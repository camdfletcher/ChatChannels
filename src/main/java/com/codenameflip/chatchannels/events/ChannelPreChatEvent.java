package com.codenameflip.chatchannels.events;

import com.codenameflip.chatchannels.structure.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 02/07/2018
 */

/**
 * Event called when the player has sent their message in chat, however it has not been displayed yet
 */
@RequiredArgsConstructor
public class ChannelPreChatEvent extends Event implements Cancellable {

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

    /**
     * Should the message not be sent to the channel?
     */
    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
