package com.codenameflip.chatchannels.commands.chat_admin;

import com.simplexitymc.command.api.ChildCommand;
import com.simplexitymc.command.api.Command;
import org.bukkit.entity.Player;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/5/18
 */
public class CmdChatAdminMute extends ChildCommand {

    public CmdChatAdminMute(Command parent, String... executors)
    {
        super(parent, executors);
    }

    @Override
    public void execute(Player player, String... strings)
    {

    }

}
