package com.codenameflip.chatchannels.commands.chat_admin;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.commands.chat.CmdChatShow;
import com.codenameflip.chatchannels.utils.Language;
import com.simplexitymc.command.api.Command;
import org.bukkit.entity.Player;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/5/18
 */
public class CmdChatAdmin extends Command {

    public CmdChatAdmin()
    {
        super(
                ChatChannels.getInstance().getCommandHandler(),
                "chatchannels.cmd.chatadmin",
                "ca", "chatadmin"
        );

        addChild(new CmdChatAdminMute(this, "mute"));
        addChild(new CmdChatAdminClear(this, "clear"));
        addChild(new CmdChatAdminDumpData(this, "dumpdata", "debug", "dbg"));
    }

    @Override
    public void execute(Player player, String... strings)
    {
        if (strings.length < 1)
        {
            msg(player, "&c&lChatChannel Commands &r&m--&r &7&oEnter a sub-command...");
            msg(player, "&c/chatAdmin mute <channel> &8- &7Prevents users from chatting in a channel.");
            msg(player, "&c/chatAdmin clear <channel> &8- &7Clears all channel messages.");
            msg(player, "&c/chatAdmin dumpData &8- &7A debug command.");
            return;
        }

        attemptChildCommand(player, strings);
    }

    /**
     * Utility method for easy colorized messages
     *
     * @param player The player you would like to send a message to
     * @param message The message you would like to send to the player
     */
    private void msg(Player player, String message)
    {
        player.sendMessage(Language.color(message));
    }

}
