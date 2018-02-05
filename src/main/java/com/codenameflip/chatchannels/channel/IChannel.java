package com.codenameflip.chatchannels.channel;

import com.codenameflip.chatchannels.ChannelViewer;
import com.codenameflip.chatchannels.utilities.Datastore;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/3/18
 */
@RequiredArgsConstructor
public abstract class IChannel implements Datastore {

    @Getter
    private final String identifier;

    @Getter
    private final String displayName;

    @Getter
    private Set<ChannelViewer> focusedPlayers = new HashSet<>();

    @Getter
    private Set<ChannelViewer> hiddenPlayers = new HashSet<>();

}
