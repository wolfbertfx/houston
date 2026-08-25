package ru.wolfbertfx.houston.common.asset;

import ru.wolfbertfx.houston.common.venue.Venue;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Инструмент — рыночный объект в системе: числовой ID (контракт для Kafka/API)
 * и неизменяемые метаданные (площадка, тип, природа, сегмент, валюта, дискавери, пайплайн обработки).
 * Не путать с Symbol — строковым кодом источника у провайдера.
 * Операционное состояние (Status, DataSource, политика склейки) живёт в control/Redis — здесь только инварианты.
 */
public enum Instrument {

    /** --- MOEX --- */
    //MO_IMOEX_CALC(10000, Venue.MOEX, Type.CALC, Nature.INDEX, Segment.EQUITY, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_RTSI_CALC(10001, Venue.MOEX, Type.CALC, Nature.INDEX, Segment.EQUITY, Currency.USD, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_RGBI_CALC(10002, Venue.MOEX, Type.CALC, Nature.INDEX, Segment.BOND, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_SBER_SPOT(10003, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_ROSN_SPOT(10004, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_LKOH_SPOT(10005, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_GAZP_SPOT(10006, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_NLMK_SPOT(10007, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_PHOR_SPOT(10008, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_GMKN_SPOT(10009, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_YDEX_SPOT(10010, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.TECH, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_SNGSP_SPOT(10011, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_AFLT_SPOT(10012, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.RETAIL, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_VTBR_SPOT(10013, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_TATN_SPOT(10014, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_MGNT_SPOT(10015, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.RETAIL, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_MOEX_SPOT(10016, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_FEES_SPOT(10017, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.UTILITIES, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_USDRUB_PERP(10018, Venue.MOEX, Type.PERPETUAL, Nature.CURRENCY, Segment.MAJORS, Currency.RUB, Discovery.ANCHORED, Pipeline.PERPETUAL), // якорь: OTC-расчётный курс + ночной дрейф доллара
    //MO_EURRUB_PERP(10019, Venue.MOEX, Type.PERPETUAL, Nature.CURRENCY, Segment.MAJORS, Currency.RUB, Discovery.ANCHORED, Pipeline.PERPETUAL), // якорь: OTC-расчётный курс + ночной дрейф евро
    MO_CNYRUB_SPOT(10020, Venue.MOEX, Type.SPOT, Nature.CURRENCY, Segment.REGIONAL, Currency.RUB, Discovery.ANCHORED, Pipeline.DIRECT); // якорь: глобальный FX, путь CNY/USD записан 24/5
    //MO_GLDRUB_SPOT(10021, Venue.MOEX, Type.SPOT, Nature.COMMODITY, Segment.METALS, Currency.RUB, Discovery.ANCHORED, Pipeline.DIRECT), // якорь: XAU/USD торгуется 24/5
    //MO_TGLD_ETF(10022, Venue.MOEX, Type.ETF, Nature.COMMODITY, Segment.METALS, Currency.RUB, Discovery.ANCHORED, Pipeline.DIRECT), // якорь: следует за золотом вне нашей сессии
    //MO_TMOS_ETF(10023, Venue.MOEX, Type.ETF, Nature.INDEX, Segment.EQUITY, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT), // компоненты торгуются на MOEX
    //MO_NVTK_SPOT(10024, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_PLZL_SPOT(10025, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT),
    //MO_BSPB_SPOT(10026, Venue.MOEX, Type.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Discovery.NATIVE, Pipeline.DIRECT);

    /** --- ICE --- */
    //IC_BR_PANAMA(11000, Venue.ICE, Type.FUTURES, Nature.COMMODITY, Segment.ENERGY, Currency.USD, Discovery.NATIVE, Pipeline.PANAMA),

    /** --- NYMEX --- */
    //NY_CL_PANAMA(12000, Venue.NYMEX, Type.FUTURES, Nature.COMMODITY, Segment.ENERGY, Currency.USD, Discovery.NATIVE, Pipeline.PANAMA),
    //NY_NG_PANAMA(12001, Venue.NYMEX, Type.FUTURES, Nature.COMMODITY, Segment.ENERGY, Currency.USD, Discovery.NATIVE, Pipeline.PANAMA),

    /** --- COMEX --- */
    //CO_GC_PANAMA(13000, Venue.COMEX, Type.FUTURES, Nature.COMMODITY, Segment.METALS, Currency.USD, Discovery.NATIVE, Pipeline.PANAMA),
    //CO_SI_PANAMA(13001, Venue.COMEX, Type.FUTURES, Nature.COMMODITY, Segment.METALS, Currency.USD, Discovery.NATIVE, Pipeline.PANAMA),

    /** --- CBOE --- */
    //CB_SPX(14000, Venue.CBOE, Type.CALC, Nature.INDEX, Segment.EQUITY, Currency.USD, Discovery.ANCHORED, Pipeline.DIRECT), // якорь: ES фьючерсы пишут путь 24/5, расчёт индекса спит — отсюда фирменные гэпы

    /** --- CME --- */
    //CM_ES(15000, Venue.CME, Type.FUTURES, Nature.INDEX, Segment.EQUITY, Currency.USD, Discovery.NATIVE, Pipeline.DIRECT);

    private final int id;
    private final Venue venue;
    private final Type type;
    private final Nature nature;
    private final Segment segment;
    private final Currency currency;
    private final Discovery discovery;
    private final Pipeline pipeline;

    private static final Map<Integer, Instrument> BY_ID;
    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Instrument::getId, e -> e));}

    Instrument(int id, Venue venue, Type type, Nature nature, Segment segment, Currency currency,
               Discovery discovery, Pipeline pipeline) {
        this.id = id; this.venue = venue; this.type = type; this.nature = nature;
        this.segment = segment; this.currency = currency; this.discovery = discovery; this.pipeline = pipeline;
    }

    public int getId() { return id; }
    public Venue getVenue() { return venue; }
    public Type getType() { return type; }
    public Nature getNature() { return nature; }
    public Segment getSegment() { return segment; }
    public Currency getCurrency() { return currency; }
    public Discovery getDiscovery() { return discovery; }
    public Pipeline getPipeline() { return pipeline; }
    public String getSymbol() { return name(); }

    public static Instrument fromId(int id) {
        var instrument = BY_ID.get(id);
        if (instrument == null) throw new IllegalArgumentException("Unknown Instrument ID: " + id);
        return instrument;
    }
}