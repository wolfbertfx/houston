package ru.wolfbertfx.houston.common.asset;

import ru.wolfbertfx.houston.common.pipeline.Strategy;
import ru.wolfbertfx.houston.common.venue.Venue;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Тикер — рыночный объект в системе: числовой ID (контракт для Kafka/API)
 * и неизменяемые метаданные (площадка, форма, природа, сегмент, валюта, дискавери, пайплайн обработки).
 * Не путать с Symbol — строковым кодом источника у провайдера.
 * Операционное состояние (Status, DataSource, политика склейки) живёт в control/Redis — здесь только инварианты.
 */
public enum Ticker {

    /** --- MOEX --- */
    //MO_IMOEX_CALC(10000, Venue.MOEX, Form.CALC, Nature.INDEX, Segment.EQUITY, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_RTSI_CALC(10001, Venue.MOEX, Form.CALC, Nature.INDEX, Segment.EQUITY, Currency.USD, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_RGBI_CALC(10002, Venue.MOEX, Form.CALC, Nature.INDEX, Segment.BOND, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_SBER_SPOT(10003, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_ROSN_SPOT(10004, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_LKOH_SPOT(10005, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_GAZP_SPOT(10006, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_NLMK_SPOT(10007, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_PHOR_SPOT(10008, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_GMKN_SPOT(10009, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_YDEX_SPOT(10010, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.TECH, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_SNGSP_SPOT(10011, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_AFLT_SPOT(10012, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.RETAIL, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_VTBR_SPOT(10013, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_TATN_SPOT(10014, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_MGNT_SPOT(10015, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.RETAIL, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_MOEX_SPOT(10016, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_FEES_SPOT(10017, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.UTILITIES, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_USDRUB_PERP(10018, Venue.MOEX, Form.PERPETUAL, Nature.CURRENCY, Segment.MAJORS, Currency.RUB, Discovery.ANCHORED, Pipeline.PERPETUAL), // якорь: OTC-расчётный курс + ночной дрейф доллара
    //MO_EURRUB_PERP(10019, Venue.MOEX, Form.PERPETUAL, Nature.CURRENCY, Segment.MAJORS, Currency.RUB, Discovery.ANCHORED, Pipeline.PERPETUAL), // якорь: OTC-расчётный курс + ночной дрейф евро
    MO_CNYRUB_SPOT(10020, Venue.MOEX, Form.SPOT, Nature.CURRENCY, Segment.REGIONAL, Currency.RUB, Discovery.ANCHORED, Strategy.DIRECT); // якорь: глобальный FX, путь CNY/USD записан 24/5
    //MO_GLDRUB_SPOT(10021, Venue.MOEX, Form.SPOT, Nature.COMMODITY, Segment.METALS, Currency.RUB, Discovery.ANCHORED, Pipeline.DIRECT), // якорь: XAU/USD торгуется 24/5
    //MO_TGLD_ETF(10022, Venue.MOEX, Form.ETF, Nature.COMMODITY, Segment.METALS, Currency.RUB, Discovery.ANCHORED, Pipeline.DIRECT), // якорь: следует за золотом вне нашей сессии
    //MO_TMOS_ETF(10022, Venue.MOEX, Form.ETF, Nature.INDEX, Segment.EQUITY, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT), // компоненты торгуются на MOEX
    //MO_NVTK_SPOT(10024, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_PLZL_SPOT(10025, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_BSPB_SPOT(10026, Venue.MOEX, Form.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT);

    /** --- ICE --- */
    //IC_BR_PANAMA(11000, Venue.ICE, Form.CONTINUOUS_FUTURES, Nature.COMMODITY, Segment.ENERGY, Currency.USD, Discovery.NATIVE, Pipeline.PANAMA),

    /** --- NYMEX --- */
    //NY_CL_PANAMA(12000, Venue.NYMEX, Form.CONTINUOUS_FUTURES, Nature.COMMODITY, Segment.ENERGY, Currency.USD, Discovery.NATIVE, Pipeline.PANAMA),
    //NY_NG_PANAMA(12001, Venue.NYMEX, Form.CONTINUOUS_FUTURES, Nature.COMMODITY, Segment.ENERGY, Currency.USD, Discovery.NATIVE, Pipeline.PANAMA),

    /** --- COMEX --- */
    //CO_GC_PANAMA(13000, Venue.COMEX, Form.CONTINUOUS_FUTURES, Nature.COMMODITY, Segment.METALS, Currency.USD, Discovery.NATIVE, Pipeline.PANAMA),
    //CO_SI_PANAMA(13001, Venue.COMEX, Form.CONTINUOUS_FUTURES, Nature.COMMODITY, Segment.METALS, Currency.USD, Discovery.NATIVE, Pipeline.PANAMA),

    /** --- CBOE --- */
    //CB_SPX(14000, Venue.CBOE, Form.CALC, Nature.INDEX, Segment.EQUITY, Currency.USD, Discovery.ANCHORED, Pipeline.DIRECT), // якорь: ES фьючерсы пишут путь 24/5, расчёт индекса спит — отсюда фирменные гэпы

    /** --- CME --- */
    //CM_ES(15000, Venue.CME, Form.CONTINUOUS_FUTURES, Nature.INDEX, Segment.EQUITY, Currency.USD, Discovery.NATIVE, Pipeline.DIRECT);

    private final int id;
    private final Venue venue;
    private final Form form;
    private final Nature nature;
    private final Segment segment;
    private final Currency currency;
    private final Discovery discovery;
    private final Strategy strategy;

    private static final Map<Integer, Ticker> BY_ID;
    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Ticker::getId, e -> e));}

    Ticker(int id, Venue venue, Form form, Nature nature, Segment segment, Currency currency,
           Discovery discovery, Strategy strategy) {
        this.id = id; this.venue = venue; this.form = form; this.nature = nature;
        this.segment = segment; this.currency = currency; this.discovery = discovery; this.strategy = strategy;
    }

    public int getId() { return id; }
    public Venue getVenue() { return venue; }
    public Form getForm() { return form; }
    public Nature getNature() { return nature; }
    public Segment getSegment() { return segment; }
    public Currency getCurrency() { return currency; }
    public Discovery getDiscovery() { return discovery; }
    public Strategy getPipeline() { return strategy; }
    public String getSymbol() { return name(); }

    public static Ticker fromId(int id) {
        var ticker = BY_ID.get(id);
        if (ticker == null) throw new IllegalArgumentException("Unknown Ticker ID: " + id);
        return ticker;
    }
}