package com.codenameflip.chatchannels.commands;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.structure.IChannelRegistry;
import org.bukkit.command.CommandExecutor;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 06/03/2018
 */
public interface ChatChannelsCommand extends CommandExecutor {

    default ChatChannels get() {
        return ChatChannels.getInstance();
    }

    default IChannelRegistry getRegistry() {
        return ChatChannels.getInstance().getRegistry();
    }

}
