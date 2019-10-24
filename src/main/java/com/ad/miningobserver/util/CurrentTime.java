package com.ad.miningobserver.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * CurrentTime
 */
public interface CurrentTime {
    
    /**
     * Current time in UTC offset.
     * 
     * @return the current date time in UTC offset
     */
    default LocalDateTime currentTimeUTC() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
