package com.codenameflip.chatchannels.structure;

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

    /**
     * The registry identifier, used for internal identification and also in the configuration file
     */
    @Getter
    public final String identifier;

    /**
     * The registry aliases, also used for internal identification and in the configuration file
     */
    @Getter
    public final List<String> aliases;

    /**
     * The equivalent of "onEnable" for the registry
     * <p>
     * Run all loading and initializing here
     */
    abstract public void construct();

    /**
     * The equivalent of "onDisable" for the registry
     * <p>
     * Run all cleanup and disposal here
     */
    abstract public void deconstruct();

    /**
     * A {@link Set} of {@link Channel} that have been registered during registry construction
     *
     * @return
     */
    abstract public Set<Channel> getChannels();

    /**
     * Retrieves an {@link Optional<Channel>} using a string identifier
     *
     * @param identifier The identifier of the target channel
     * @return {@link Optional<Channel>}, if present then a channel could be found, otherwise it couldn't
     */
    abstract public Optional<Channel> getChannel(String identifier);

}
