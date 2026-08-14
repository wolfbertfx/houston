/* Copyright (c) 2026 LLC "Radaps". All rights reserved. Internal Use Only. Confidential.*/
package com.radaps.ewaspace.houston.shared.asset;

import com.radaps.ewaspace.houston.shared.Identifiable;
import com.radaps.ewaspace.houston.shared.Localizable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Ticker implements Localizable, Identifiable {

    /** --- MOEX --- */
    M_IMOEX_CALC(10000), M_RTSI_CALC(10001), M_RGBI_CALC(10002),
    M_SBER_SPOT(10003), M_ROSN_SPOT(10004), M_LKOH_SPOT(10005),
    M_GAZP_SPOT(10006), M_NLMK_SPOT(10007), M_PHOR_SPOT(10008),
    M_GMKN_SPOT(10009), M_YDEX_SPOT(10010), M_SNGSP_SPOT(10011),
    M_AFLT_SPOT(10012), M_VTBR_SPOT(10013), M_TATN_SPOT(10014),
    M_MGNT_SPOT(10015), M_MOEX_SPOT(10016), M_FEES_SPOT(10017),
    M_USDRUB_PERP(10018), M_EURRUB_PERP(10019), M_CNYRUB_SPOT(10020),
    M_GLDRUB_SPOT(10021), M_TGLD_ETF(10022), M_TMOS_ETF(10023),
    M_NVTK_SPOT(10024), M_PLZL_SPOT(10025), M_BSPB_SPOT(10026),

    //* --- ICE --- */
    ICE_BR_PANAMA(2000),

    // --- NYMEX ---
    NYMEX_CL_PANAMA(3010),
    NYMEX_NG_PANAMA(3020),

    // --- COMEX ---
    COMEX_GC_PANAMA(4030),
    COMEX_SI_PANAMA(4040),

    // --- CBOE ---
    CBOE_SPX(5000),

    // --- CME ---
    CME_ES(6001);

    private final int id;
    private static final Map<Integer, Ticker> BY_ID;
    private static final Map<String, Ticker> BY_NAME;

    static {
        BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Ticker::getId, e -> e));
        BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(e -> e.name().toUpperCase(), e -> e));
    }

    Ticker(int id) {this.id = id;}

    public String getSymbol() {return name();}

    public static Ticker fromId(int id) {
        var ticker = BY_ID.get(id);
        if (ticker == null) throw new IllegalArgumentException("Unknown AssetTicker ID: " + id);
        return ticker;
    }

    public static Ticker fromString(String name) {
        var ticker = BY_NAME.get(name.toUpperCase());
        if (ticker == null) throw new IllegalArgumentException("Unknown AssetTicker name: " + name);
        return ticker;
    }

    @Override
    public int getId() {return id;}

    @Override
    public String getLocaleKey() {
        return "asset.ticker." + this.name().toLowerCase();
    }
}
