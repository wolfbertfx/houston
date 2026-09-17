# Houston

Houston — это приватный слой экспертной системы в рамках R&D проекта для сбора, обработки и анализа рыночных данных. Архитектура построена на гибридной модели Event-Driven Architecture (EDA), сочетающей паттерны **Pipes and Filters** (Kafka-пайплайн потоковой обработки) и **Blackboard** (аккумулирование состояния в TimescaleDB / Redis для итогового аналитического синтеза).

### Модули монорепозитория

* `common/` — общие контракты, доменные модели и перечисления (enums). Никакой логики конкретных сервисов.
* `console/` — фронтенд для Control Center.
* `control/` — расписание бирж, мониторинг метрик, дебаг-режим с реплеем аналитики (time-travel). Слой **политик**: режимы активов, конфигурация источников данных.
* `guidance/` — управление жизненным циклом состояний форваторов (fairway).
* `ingestion/` — сбор сырых данных (5-минутные свечи) с провайдеров и публикация в шину. Диспетчер provider-адаптеров.
* `navigation/` — расчет гипотез (Эллиотт + Хёрст + Байес), скоринг. Тяжелая CPU/memory-bound математика.
* `processing/` — склейка фьючерсов по методу PANAMA (синтез), запись в базу. I/O-bound потоковый конвейер.
* `uplink/` — мост, стримящий риалтайм-данные наружу в облако по защищенному gRPC.

## Технологический стек

* **Сервисы:** Quarkus + Java 21 (Virtual Threads);
* **Инфраструктура:**
  * Kafka / Native Kafka (шина событий для свечей и системных событий)
  * Redis (кэш, скользящее окно свечей, runtime-статусы активов, конфигурация склейки)
  * TimescaleDB + PostgreSQL (два отдельных инстанса, см. ниже)
  * Grafana Loki (централизованный сбор логов с трассировкой из Docker для отладки пайплайнов)
* **Гибридный контур:**
  * Холодный слой: нативная репликация TimescaleDB из мастер-базы в облачную реплику через Tailscale.
  * Горячий слой: риалтайм-поток через gRPC uplink к облачному Quarkus модулю.

## Данные и схема потока

Два **отдельных** инстанса БД (разделение по характеру нагрузки, не по воле сервисов):

| Контейнер | Порт (host) | Базы | Заточен под | Кто пишет | Кто читает |
|---|---|---|---|---|---|
| `timescaledb` | **5433** | `market_data` | OLAP / time-series / bulk insert | `processing` (единственный писатель) | `navigation` (тяжелые OLAP-сканы за 6 лет), `ingestion` (редкий `max(ts)`) |
| `postgres:15-alpine` | **5432** | `control`, `navigation`, `guidance` | OLTP / стейты | `control`, `navigation`, `guidance` | те же |

> Правило разделения: **источник истины всегда в персистентном слое (БД), Redis — только кэш/подсказка поверх него.**

### Поток данных

```
ingestion ──raw_candles(kafka, key=ticker)──▶ processing ──write──▶ market_data (timescale, 5433)
                                                │
                                                └──candle_synthesized + watermark в Redis──▶ guidance / console

navigation ──read 6 лет истории из market_data──▶ массив гипотез (компактный: текущий стек волн,
                                                 H-коэффициент, invalidation-уровни, P(H|E))
                                                 → сохраняет в свою БД + пушит событие в Kafka

guidance ──слушает Kafka + читает Redis (окно 3-6 мес) + берет гипотезы──▶ fairway → запись в свою БД
```

* **Kafka-партиционирование** по `ticker` (key = asset_id) — гарантирует строгий порядок свечей внутри инструмента.
* Свечи **идемпотентны по `(ticker, ts)`** — приём at-least-once + upsert на записи. Никогда не стартуем «впереди» точки персистентности.

## Ключевые архитектурные решения

### 1. `processing` и `navigation` — разные модули (не объединять)
* `processing` — I/O-bound, непрерывный поток, минимальный лаг в Kafka.
* `navigation` — CPU/memory-bound батчи: поднимает 6 лет истории, гоняет алгоритмы сверху вниз/снизу вверх. Объединение = лаги в Kafka и GC-паузы на математике.
* Общая БД для них — это не нарушение MSA, а **Blackboard-паттерн**: один писатель (`processing`), читатели (`navigation`, `ingestion`), никаких гонок.

### 2. Склейка PANAMA: Policy (control) vs Execution (processing)
* **control** задаёт **политику** в Redis (`config:splice:{ticker}`): метод (`VOLUME` / `BY_DATE`), порог, `days_before_expiry`, цепочка контрактов. Ему не нужны объёмы — он говорит *как* решать, не *когда*.
* **processing** исполняет: считает ролл на реальных данных, публикует `roll_executed {ticker, from_contract, to_contract, roll_date, ratio}`, пишет в таблицу `splices` в `market_data`.
* **Версионирование склейки:** `continuous` и `splices` хранят `splice_version`. Смена метода в админке → инкремент версии → событие `splice_rebuild` в Kafka → processing пересобирает **только затронутый тикер из raw** (raw никогда не переписывается). Старые версии можно держать для A/B.

### 3. Ingestion: Provider ≠ Venue
* **Venue** — статичная метадата биржи (таймзона, сессия) в `common`.
* **Provider** — операционный транспорт/вендор (`MOEX_ISS`, `BARCHART`, `CSV_FILE`). Хранится в БД control как сущность **`DataSource`**: `{ticker_id, role, provider, symbol, timeframe, poll_ms, params}`. Правится в админке, не в коде.
* Ingestion — **тонкий диспетчер** над реестром адаптеров `DataProvider` (`MoexIssProvider`, `BarchartProvider`, ...). Никаких `if (venue == ICE)` в core.
* Panama-тикер маппится на **несколько** raw-источников — по одному на контракт-месяц (`BRN1!`, `BRN2!`, ...).

### 4. BACKFILL / LIVE — производная от гэпа, а не режим
* Один механизм: job `{ticker, contract, from, to}`. Воркер при старте считает `gap = now - max(ts)`:
  * `gap` мал → LIVE (single fetch),
  * `gap` велик → BACKFILL (чанки, rate limit, прогресс `hops_done/hops_total`).
* Переключение **двустороннее и автоматическое**. Онбординг: пустой watermark → BACKFILL → догнал → LIVE. Простой сервера на неделю → сам перещёлкнется в докачку.
* **Watermark — правда в БД** (`max(ts)` per `(ticker, contract)`), в Redis — кэш-подсказка (пишется processing **после** commit, порядок строго: БД → Redis).
* Обязательно: concurrency cap на все тикеры + token bucket на провайдера; учитывать **ретенцию провайдера** (не все могут отдать год истории).

### 5. Режимы и статусы актива
* **`Status`** (`common/asset`) — воля админа + readiness gate:
  * `DISABLED(0)` — архив, не качается, не виден
  * `PREPARING(1)` — докачка/прогрев, качается, **скрыт** от console/guidance
  * `ENABLED(2)` — работает, качается, виден всем
* **`State`** (`common/pipeline`) — производное состояние ingestion:
  * `IDLE` — простоит
  * `BACKFILL` — докачка истории
  * `LIVE` — штатный поллинг
* **Правило доступа для аналитики (navigation/guidance/console):**
  * Видит актив **только если** `Status == ENABLED` **И** `State == LIVE`
  * `PREPARING` — ingestion качает, но аналитика не подключена (гарантия качества данных)

### 6. Кэш активов в Redis (Sets по venue)
| Ключ | Владелец | Содержимое | Потребители |
|---|---|---|---|
| `venue:{venue}:assets:enabled` | Control | `instrumentId` со `Status=ENABLED` | Ingestion (качает), Console |
| `venue:{venue}:assets:preparing` | Control | `instrumentId` со `Status=PREPARING` | Ingestion (качает) |
| `venue:{venue}:assets:live` | **Ingestion** | `instrumentId` в `State=LIVE` | Navigation, Guidance |

* Ingestion читает `SUNION(enabled, preparing)` → список к качке.
* При переходе `BACKFILL → LIVE` ingestion делает `SADD venue:{venue}:assets:live {id}`.
* Navigation/Guidance читают **только** `live` сет.
* Console читает `enabled` (показывает и BACKFILL, и LIVE).

### 7. Скользящее окно в Redis
* 300 активов × 6 мес 5-минуток ≈ 420 МБ — копейки для RAM.
* Redis `ZSET` (`score = ts_epoch_ms`), добавление `ZADD`, обрезка хвоста `ZREMRANGEBYSCORE`. Служит для `guidance` (мини-волновка каждые 5 мин) и `console` (графики), разгружает timescale от частых мелких чтений.

## Быстрый старт

1. Запуск инфраструктуры (Redis, Kafka, timescaledb, postgres, Loki):
   ```bash
   ./dev_env.sh
   ```
   или напрямую `docker compose -f dev_env_compose.yml up -d`.

2. **Важно:** при переходе на двухбазовую схему снести старый volume от старого единого postgres (данные timescale несовместимы с ванильным PG):
   ```bash
   docker compose -f dev_env_compose.yml down
   rm -rf /Users/wolfbertfx/Documents/Docker/houston/postgres
   ./dev_env.sh
   ```

3. Сборка модулей — Maven wrapper внутри директорий:
   ```bash
   cd common && ../control/mvnw install -DskipTests   # сначала common (библиотека, своего mvnw нет)
   cd control && ./mvnw compile                       # затем любой сервис
   ```
   Все сервисы зависят от `common`, поэтому его установка (`install`) обязательна перед сборкой остальных.

### Подключения к БД (dev)
* Стейты/бизнес: `localhost:5432` (postgres) — базы `control`, `navigation`, `guidance`.
* Котировки: `localhost:5433` (timescaledb) — база `market_data`.
* Инициализация баз и расширений — `init-multiple-dbs.sh` (монтируется в оба контейнера, сам определяет наличие timescaledb-расширения).

---

## Статус реализации (на текущий момент)

### ✅ Реализовано
| Модуль | Что готово |
|---|---|
| **common** | Все enums (`Asset`, `Venue`, `Currency`, `Type`, `Nature`, `Segment`, `Discovery`, `Status`, `Pipeline`, `Provider`, `Roll`, `Phase`, `State`), `Redis` keys class |
| **control** | `Asset` domain + REST API (`GET /api/control/assets`, `PATCH /api/control/assets/{id}/status`), JPA persistence, Liquibase миграция, Redis cache (`AssetRedisCache`) с venue-based Sets, Fault Tolerance retry/fallback, out-of-sync флаг + scheduled reconciliation job |
| **infra** | Docker compose (Redis, Kafka, TimescaleDB, Postgres, Loki), dev_env.sh |

### 🚧 В планах / не начато
| Модуль | Что нужно |
|---|---|
| **control** | `DataSource` CRUD + валидация, Splice policy API (`config:splice:{ticker}`), Venue/Exchange calendar, Metrics/health endpoints |
| **ingestion** | Provider dispatcher, MOEX_ISS / Barchart adapters, watermark logic (BACKFILL/LIVE), Kafka producer для `raw_candles`, управление `venue:*:assets:live` сетом |
| **navigation** | Wave/Hurst/Bayes engine, hypothesis storage, Kafka consumer для свечей |
| **guidance** | Fairway lifecycle, Redis window reader (ZSET), hypothesis consumer |
| **processing** | Panama splice engine, continuous series writer, roll execution, Kafka consumer |
| **uplink** | gRPC streaming к облаку |
| **console** | Frontend (React/Vue + charts) |

---

## Полезные команды

```bash
# Полная чистая сборка
cd common && ../control/mvnw clean install -DskipTests
cd control && ./mvnw clean compile -DskipTests

# Запуск инфраструктуры
./dev_env.sh

# Остановка с чисткой volumes (переход на новую схему БД)
docker compose -f dev_env_compose.yml down
rm -rf /Users/wolfbertfx/Documents/Docker/houston/postgres
./dev_env.sh
```