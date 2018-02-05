package com.codenameflip.chatchannels.utils;

import lombok.Builder;

import java.util.HashMap;

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

    public Placeholders()
    {
    }

    /**
     * Assigns a string identifier to a replacement object
     * @param matcher The string you would like to replace
     * @param replacement The object you would like to replace it with
     * @return this (builder)
     */
    public Placeholders put(String matcher, Object replacement)
    {
        VALUES.put(matcher, replacement);
        return this;
    }

    /**
     * Returns the map that is readable by the locale methods
     * @return A map readable by {@link Language} functions
     */
    public HashMap<String, Object> build()
    {
        return VALUES;
    }

}
