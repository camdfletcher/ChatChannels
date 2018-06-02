package com.codenameflip.chatchannels.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/4/18
 */

/**
 * A utility class to make it easier to use the locale methods in {@link Language}
 */
public class Placeholders {

    private static final HashMap<String, Object> VALUES = new HashMap<>();

    public Placeholders() {
    }

    public Placeholders(String key, Object value) { // For simple one-value placeholders
        VALUES.put(key, value);
    }

    /**
     * Assigns a string identifier to a replacement object
     *
     * @param matcher     The string you would like to replace
     * @param replacement The object you would like to replace it with
     * @return this (builder)
     */
    public Placeholders put(String matcher, Object replacement) {
        VALUES.put(matcher, replacement);
        return this;
    }

    /**
     * Returns the map that is readable by the locale methods
     *
     * @return A map readable by {@link Language} functions
     */
    public HashMap<String, Object> build() {
        return VALUES;
    }

    /**
     * Attempts to resolve all placeholders in a passed string
     *
     * @param base The string you'd like to convert placeholders for
     * @return The newly generated string
     */
    public String attemptMatch(String base) {
        String newString = base;

        for (Map.Entry<String, Object> placeholder : build().entrySet())
            newString = newString.replaceAll(placeholder.getKey(), placeholder.getValue().toString());

        return newString;
    }

}
