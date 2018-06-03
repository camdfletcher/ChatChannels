package com.codenameflip.chatchannels.commands;


import com.codenameflip.chatchannels.structure.Channel;
import com.codenameflip.chatchannels.utils.Language;
import com.codenameflip.chatchannels.utils.Placeholders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/5/18
 */
public class CmdChat implements ChatChannelsCommand {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length == 0) {
            msg(player, "&6&lChatChannel Commands &r&m--&r &e&oEnter a sub-command...");
            msg(player, " &6/chat &ls&6how <channel> &8- &7Displays a channel's chat.");
            msg(player, " &6/chat &lh&6ide <channel> &8- &7Hides a channel's chat.");
            msg(player, " &6/chat &lf&6ocus <channel> &8- &7Sets your channel focus.");
        } else if (args.length >= 2) {
            Optional<Channel> targetChannel = getRegistry().getChannel(args[1]);

            if (!targetChannel.isPresent()) {
                Language.localeChat(player, "INVALID_PARAM", new Placeholders("param", "channel").build());
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "show":
                case "s":
                case "view":
                    getRegistry().showChannel(player, targetChannel.get());
                    break;
                case "hide":
                case "h":
                case "goaway":
                    getRegistry().hideChannel(player, targetChannel.get());
                    break;
                case "focus":
                case "f":
                case "talk":
                    getRegistry().focusChannel(player, targetChannel.get());
                    break;
                default:
                    Language.localeChat(player, "INVALID_USAGE", null);
                    break;
            }
        } else Language.localeChat(player, "INVALID_USAGE", null);

        return true;
    }

    /**
     * Utility method for easy colorized messages
     *
     * @param player  The player you would like to send a message to
     * @param message The message you would like to send to the player
     */
    private void msg(Player player, String message) {
        player.sendMessage(Language.color(message));
    }

}
