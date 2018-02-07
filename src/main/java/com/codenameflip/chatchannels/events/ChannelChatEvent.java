package com.codenameflip.chatchannels.events;

import com.codenameflip.chatchannels.structure.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 02/07/2018
 */

/**
 * Event called after the message has been processed and is now going to be sent
 */
@RequiredArgsConstructor
public class ChannelChatEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    /**
     * The player sending the message
     */
    @Getter
    private final Player player;

    /**
     * The {@link Channel} the message will be sent in
     */
    @Getter
    private final Channel channel;

    /**
     * The message the player is sending
     */
    @Getter
    private final String message;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
