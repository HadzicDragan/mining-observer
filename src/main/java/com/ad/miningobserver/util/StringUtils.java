package com.ad.miningobserver.util;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * StringUtils
 */
public class StringUtils {
    
    /**
     * Join the string array together.
     * 
     * @param values {@code String} to join together
     * @return concatenated {@code String} value from param
     */
    public static String buildString(String... values) {
        return Stream.of(values)
                .collect(Collectors.joining());
    }
    
    /**
     * Generate random UUID {@code String} value.
     * 
     * @return random generated UUID {@code String} value
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
