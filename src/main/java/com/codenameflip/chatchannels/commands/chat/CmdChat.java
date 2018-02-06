package com.codenameflip.chatchannels.commands.chat;


import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.utils.Language;
import com.simplexitymc.command.api.Command;
import org.bukkit.entity.Player;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/5/18
 */
public class CmdChat extends Command {

    public CmdChat()
    {
        super(
                ChatChannels.getInstance().getCommandHandler(),
                "chatchannels.cmd.chat",
                "c", "chat", "chatchannels"
        );

        addChild(new CmdChatShow(this, "s", "show"));
        addChild(new CmdChatHide(this, "h", "hide"));
        addChild(new CmdChatFocus(this, "f", "focus"));
    }

    @Override
    public void execute(Player player, String... strings)
    {
        if (strings.length < 1)
        {
            msg(player, "&6&lChatChannel Commands &r&m--&r &7&oEnter a sub-command...");
            msg(player, "&6/chat &ls&6how <channel> &8- &7Displays a channel's chat.");
            msg(player, "&6/chat &lh&6ide <channel> &8- &7Hides a channel's chat.");
            msg(player, "&6/chat &lf&6ocus <channel> &8- &7Sets your channel focus.");

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
