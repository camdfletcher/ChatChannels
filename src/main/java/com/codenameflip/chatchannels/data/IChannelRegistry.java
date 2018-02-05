package com.codenameflip.chatchannels.data;

import com.codenameflip.chatchannels.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/4/18
 */
@RequiredArgsConstructor
public abstract class IChannelRegistry {

    @Getter
    public final String identifier;

    @Getter
    public final List<String> aliases;

    abstract public void construct();

    abstract public void deconstruct();

    abstract public Set<Channel> getChannels();

    abstract public Optional<Channel> getChannel(String identifier);


}
