package com.codenameflip.chatchannels.structure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/3/18
 */
@RequiredArgsConstructor
public class Channel {

    /**
     * The identifier that the API will use to reference the channel, specified in the
     */
    @Getter
    private final String identifier;

    /**
     * The formatted, pretty name for the channel; used for all front-end displays
     */
    @Getter
    private final String displayName;

    /**
     * All string aliases that can also be used to reference this channel; primarily used for commands
     */
    @Getter
    private final List<String> aliases;

    /**
     * All properties that influence how the channel functions. See: {@link ChannelProperties}
     */
    @Getter
    private final ChannelProperties properties;

    /**
     * A list of viewers currently focused/chatting in the channel
     */
    @Getter
    private Set<ChannelViewer> focusedPlayers = new HashSet<>();

    /**
     * A list of viewers who currently have the channel hidden
     */
    @Getter
    private Set<ChannelViewer> hiddenPlayers = new HashSet<>();


}
