package com.ad.miningobserver.util;

import java.time.temporal.TemporalUnit;

/**
 * DateReference 
 * 
 */
public class DateReference {
    
    private final long amount;
    private final TemporalUnit unit;

    public DateReference(long amount, TemporalUnit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    /**
     * @return the ammount presented in {@code long} value
     */
    public long getAmount() {
        return amount;
    }

    /**
     * @return {@code TemporalUnit} for the ammount, to 
     * know which unit of time it should represent
     */
    public TemporalUnit getUnit() {
        return unit;
    }
}
