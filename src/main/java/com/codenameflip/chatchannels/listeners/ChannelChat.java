package com.codenameflip.chatchannels.listeners;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.events.ChannelChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author codenameflip
 * @since 4/8/17
 */

public class ChannelChat implements Listener {

    @EventHandler
    public void onChannelChat(ChannelChatEvent event)
    {
        if (ChatChannels.get().getConfig().getBoolean("chat-settings.log-messages"))
            System.out.println(" ** [CHAT LOG] (" + event.getTargetChannel().getName() + ") " + event.getSender().getName() + ": " + event.getMessage());
    }

}
