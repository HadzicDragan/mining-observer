package com.ad.miningobserver.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * CurrentTime
 */
public class CurrentTime {
    
    /**
     * Current time in UTC offset.
     * 
     * @return the current date time in UTC offset
     */
    public static LocalDateTime currentTimeUTC() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
