package com.codenameflip.chatchannels.utils;

import lombok.Builder;

import java.util.HashMap;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/4/18
 */
public class Placeholders {

    private static final HashMap<String, Object> VALUES = new HashMap<>();

    public Placeholders()
    {
    }

    public Placeholders put(String matcher, Object replacement)
    {
        VALUES.put(matcher, replacement);
        return this;
    }

    public HashMap<String, Object> build()
    {
        return VALUES;
    }

}
