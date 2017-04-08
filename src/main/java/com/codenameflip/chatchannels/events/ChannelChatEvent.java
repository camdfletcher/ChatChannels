package com.codenameflip.chatchannels.events;

import com.codenameflip.chatchannels.channels.Channel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author codenameflip
 * @since 4/8/17
 */

public class ChannelChatEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Player sender;
    private Channel targetChannel;
    private String message;

    public ChannelChatEvent(Player sender, Channel targetChannel, String message) {
        this.sender = sender;
        this.targetChannel = targetChannel;
        this.message = message;
    }

    private boolean cancelled;

    public Player getSender() {
        return sender;
    }

    public Channel getTargetChannel() {
        return targetChannel;
    }

    public String getMessage() {
        return message;
    }

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
