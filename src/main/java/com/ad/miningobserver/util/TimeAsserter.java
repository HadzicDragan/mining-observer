package com.ad.miningobserver.util;

import java.time.Instant;

/**
 * Time based operations
 */
public class TimeAsserter {
    
    /**
     * Reduce the current time {@code Instant} with the passed param.
     * 
     * @param dateRef ammount of time and time unit
     * @return reduced {@code Instant}
     */
    public static Instant reducedInstantFromNow(final DateReference dateRef) {
        return Instant.now().minus(dateRef.getAmount(), dateRef.getUnit());
    }
}
