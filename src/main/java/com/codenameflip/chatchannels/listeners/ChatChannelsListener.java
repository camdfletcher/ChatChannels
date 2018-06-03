package com.codenameflip.chatchannels.listeners;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.structure.IChannelRegistry;
import org.bukkit.event.Listener;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 02/07/2018
 */

/**
 * Utility class designed to reduce repeating calls for main class instance and registry calls
 */
public interface ChatChannelsListener extends Listener {

    default ChatChannels get() {
        return ChatChannels.getInstance();
    }

    default IChannelRegistry getRegistry() {
        return ChatChannels.getInstance().getRegistry();
    }

}
