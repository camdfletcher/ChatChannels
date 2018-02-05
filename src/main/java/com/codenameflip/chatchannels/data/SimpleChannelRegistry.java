package com.codenameflip.chatchannels.data;

import com.codenameflip.chatchannels.channel.Channel;

import java.util.*;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/4/18
 */
public class SimpleChannelRegistry extends IChannelRegistry {

    private final HashSet<Channel> CHANNELS = new HashSet<>();

    public SimpleChannelRegistry()
    {
        super("simple", Arrays.asList("config", "configuration"));
    }

    @Override
    public void construct()
    {
    }

    @Override
    public void deconstruct()
    {
    }

    @Override
    public Set<Channel> getChannels()
    {
        return CHANNELS;
    }

    @Override
    public Optional<Channel> getChannel(String identifier)
    {
        return Optional.empty();
    }

}
