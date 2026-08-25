package ru.wolfbertfx.houston.common.pipeline;

import ru.wolfbertfx.houston.common.asset.Venue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Идентичность транспорта рыночных данных (контракт для control/ingestion/Kafka-событий).
 * Назначение «инструмент → провайдер» живёт в control (DataSource), реализация — адаптеры в ingestion.
 * Не путать с Venue — площадкой, где рождается цена: провайдер лишь объявляет, какие Venue способен обслуживать.
 */
public enum Provider {

    /** Официальный ISS API Московской биржи. */
    MOEX_ISS(1, EnumSet.of(Venue.MOEX)),
    /** Barchart market data (в т.ч. непрерывные серии и инструменты вне прямого доступа). */
    BARCHART(2, EnumSet.of(Venue.ICE, Venue.NYMEX, Venue.COMEX, Venue.CME, Venue.CBOE));
    /** Импорт истории из файла — id зарезервирован, механика не спроектирована. */
    //CSV_FILE(3, EnumSet.noneOf(Venue.class));

    private final int id;
    private final Set<Venue> venues;

    private static final Map<Integer, Provider> BY_ID;

    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Provider::getId, e -> e));}

    Provider(int id, Set<Venue> venues) {this.id = id; this.venues = venues;}

    public int getId() {return id;}
    public Set<Venue> getVenues() {return venues;}

    /** Может ли провайдер обслуживать площадку — фильтр дропдауна в UI. */
    public boolean supports(Venue venue) {return venues.contains(venue);}

    public static Provider fromId(int id) {
        var provider = BY_ID.get(id);
        if (provider == null) throw new IllegalArgumentException("Unknown Provider ID: " + id);
        return provider;
    }
}