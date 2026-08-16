package ru.wolfbertfx.houston.common.asset;

import ru.wolfbertfx.houston.common.exchange.Currency;
import ru.wolfbertfx.houston.common.exchange.Venue;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Исчерпывающий статический реестр торговых инструментов и синтетических активов (например, склеек по методу PANAMA).
 * Содержит жестко зашитые числовые ID для оптимизированной передачи в Kafka (без строк)
 * и неизменяемые метаданные (биржа, инструмент, природа, сегмент, валюта, пайплайн обработки).
 */
public enum Ticker {

    /** --- MOEX --- */
    //MO_IMOEX_CALC(10000, Venue.MOEX, Instrument.SPOT, Nature.INDEX, Segment.EQUITY, Currency.RUB, Pipeline.DIRECT),
    //MO_RTSI_CALC(10001, Venue.MOEX, Instrument.SPOT, Nature.INDEX, Segment.EQUITY, Currency.USD, Pipeline.DIRECT),
    //MO_RGBI_CALC(10002, Venue.MOEX, Instrument.SPOT, Nature.INDEX, Segment.BOND, Currency.RUB, Pipeline.DIRECT),
    //MO_SBER_SPOT(10003, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Pipeline.DIRECT),
    //MO_ROSN_SPOT(10004, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Pipeline.DIRECT),
    //MO_LKOH_SPOT(10005, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Pipeline.DIRECT),
    //MO_GAZP_SPOT(10006, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Pipeline.DIRECT),
    //MO_NLMK_SPOT(10007, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Pipeline.DIRECT),
    //MO_PHOR_SPOT(10008, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Pipeline.DIRECT),
    //MO_GMKN_SPOT(10009, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Pipeline.DIRECT),
    //MO_YDEX_SPOT(10010, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.TECH, Currency.RUB, Pipeline.DIRECT),
    //MO_SNGSP_SPOT(10011, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Pipeline.DIRECT),
    //MO_AFLT_SPOT(10012, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.RETAIL, Currency.RUB, Pipeline.DIRECT),
    //MO_VTBR_SPOT(10013, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Pipeline.DIRECT),
    //MO_TATN_SPOT(10014, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Pipeline.DIRECT),
    //MO_MGNT_SPOT(10015, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.RETAIL, Currency.RUB, Pipeline.DIRECT),
    //MO_MOEX_SPOT(10016, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Pipeline.DIRECT),
    //MO_FEES_SPOT(10017, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.UTILITIES, Currency.RUB, Pipeline.DIRECT),
    //MO_USDRUB_PERP(10018, Venue.MOEX, Instrument.PERPETUAL, Nature.CURRENCY, Segment.MAJORS, Currency.RUB, Pipeline.PERPETUAL),
    //MO_EURRUB_PERP(10019, Venue.MOEX, Instrument.PERPETUAL, Nature.CURRENCY, Segment.MAJORS, Currency.RUB, Pipeline.PERPETUAL),
    MO_CNYRUB_SPOT(10020, Venue.MOEX, Instrument.SPOT, Nature.CURRENCY, Segment.REGIONAL, Currency.RUB, Pipeline.DIRECT);
    //MO_GLDRUB_SPOT(10021, Venue.MOEX, Instrument.SPOT, Nature.COMMODITY, Segment.METALS, Currency.RUB, Pipeline.DIRECT),
    //MO_TGLD_ETF(10022, Venue.MOEX, Instrument.ETF, Nature.COMMODITY, Segment.METALS, Currency.RUB, Pipeline.DIRECT),
    //MO_TMOS_ETF(10023, Venue.MOEX, Instrument.ETF, Nature.INDEX, Segment.EQUITY, Currency.RUB, Pipeline.DIRECT),
    //MO_NVTK_SPOT(10024, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.OIL_GAS, Currency.RUB, Pipeline.DIRECT),
    //MO_PLZL_SPOT(10025, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.MINING, Currency.RUB, Pipeline.DIRECT),
    //MO_BSPB_SPOT(10026, Venue.MOEX, Instrument.SPOT, Nature.EQUITY, Segment.FINANCE, Currency.RUB, Pipeline.DIRECT);

    /** --- ICE --- */
    //IC_BR_PANAMA(11000, Venue.ICE, Instrument.FUTURES, Nature.COMMODITY, Segment.ENERGY, Currency.USD, Pipeline.PANAMA),

    /** --- NYMEX --- */
    //NY_CL_PANAMA(12000, Venue.NYMEX, Instrument.FUTURES, Nature.COMMODITY, Segment.ENERGY, Currency.USD, Pipeline.PANAMA),
    //NY_NG_PANAMA(12001, Venue.NYMEX, Instrument.FUTURES, Nature.COMMODITY, Segment.ENERGY, Currency.USD, Pipeline.PANAMA),

    /** --- COMEX --- */
    //CO_GC_PANAMA(13000, Venue.COMEX, Instrument.FUTURES, Nature.COMMODITY, Segment.METALS, Currency.USD, Pipeline.PANAMA),
    //CO_SI_PANAMA(13001, Venue.COMEX, Instrument.FUTURES, Nature.COMMODITY, Segment.METALS, Currency.USD, Pipeline.PANAMA),

    /** --- CBOE --- */
    //CB_SPX(14000, Venue.CBOE, Instrument.SPOT, Nature.INDEX, Segment.EQUITY, Currency.USD, Pipeline.DIRECT),

    /** --- CME --- */
    //CM_ES(15000, Venue.CME, Instrument.FUTURES, Nature.INDEX, Segment.EQUITY, Currency.USD, Pipeline.DIRECT);

    private final int id;
    private final Venue venue;
    private final Instrument instrument;
    private final Nature nature;
    private final Segment segment;
    private final Currency currency;
    private final Pipeline pipeline;

    private static final Map<Integer, Ticker> BY_ID;
    static {BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Ticker::getId, e -> e));}

    Ticker(int id, Venue venue, Instrument instrument, Nature nature, Segment segment, Currency currency, Pipeline pipeline) {
        this.id = id; this.venue = venue; this.instrument = instrument; this.nature = nature;
        this.segment = segment; this.currency = currency; this.pipeline = pipeline;
    }

    public int getId() { return id; }
    public Venue getVenue() { return venue; }
    public Instrument getInstrument() { return instrument; }
    public Nature getNature() { return nature; }
    public Segment getSegment() { return segment; }
    public Currency getCurrency() { return currency; }
    public Pipeline getPipeline() { return pipeline; }
    public String getSymbol() { return name(); }

    public static Ticker fromId(int id) {
        var ticker = BY_ID.get(id);
        if (ticker == null) throw new IllegalArgumentException("Unknown AssetTicker ID: " + id);
        return ticker;
    }
}
