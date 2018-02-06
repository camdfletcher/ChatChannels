package com.codenameflip.chatchannels.commands.chat;

import com.simplexitymc.command.api.ChildCommand;
import com.simplexitymc.command.api.Command;
import org.bukkit.entity.Player;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/5/18
 */
public class CmdChatShow extends ChildCommand {

    public CmdChatShow(Command parent, String... executors)
    {
        super(parent, executors);
    }

    @Override
    public void execute(Player player, String... strings)
    {

    }

}
