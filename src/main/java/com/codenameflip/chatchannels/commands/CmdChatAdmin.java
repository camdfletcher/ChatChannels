package com.codenameflip.chatchannels.commands;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.structure.Channel;
import com.codenameflip.chatchannels.structure.IChannelRegistry;
import com.codenameflip.chatchannels.utils.Language;
import com.codenameflip.chatchannels.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/5/18
 */
public class CmdChatAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length == 0) {
            msg(player, "&c&lChatChannel Admin Commands &r&m--&r &7&oEnter a sub-command...");
            msg(player, " &c/chatAdmin mute <channel> &8- &7Prevents users from chatting.");
            msg(player, " &c/chatAdmin clear <channel> &8- &7Clears all channel messages.");
            msg(player, " &c/chatAdmin reload &8- &7Reloads the current data registry.");

            return true;
        } else {
            if (args.length == 2) {
                Optional<Channel> targetChannel = ChatChannels.getInstance().getRegistry().getChannel(args[1]);
                Channel channel = targetChannel.get();

                if (!targetChannel.isPresent()) {
                    Language.localeChat(player, "INVALID_PARAM", new Placeholders("param", "channel").build());
                    return true;
                }

                if (args[0].equalsIgnoreCase("mute")) {
                    boolean muted = !channel.getProperties().isMuted(); // Invert the boolean to "toggle" the mute
                    channel.getProperties().setMuted(muted);

                    if (muted) channel.broadcast("CHAT_MUTED", new Placeholders("executor", player.getName()).build());
                    else channel.broadcast("CHAT_UNMUTED", new Placeholders("executor", player.getName()).build());
                } else if (args[0].equalsIgnoreCase("clear")) {
                    for (int i = 0; i < 200; i++) channel.broadcastRaw(" ");
                    channel.broadcast("CHAT_CLEARED", new Placeholders("executor", player.getName()).build());
                } else Language.localeChat(player, "INVALID_USAGE", null);
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    IChannelRegistry registry = ChatChannels.getInstance().getRegistry();

                    ChatChannels.getInstance().reloadConfig();
                    registry.deconstruct();
                    registry.construct();

                    Bukkit.getOnlinePlayers().forEach(online -> {
                        registry.getAutoShowChannels().forEach(all -> registry.showChannel(online, all));
                        registry.getAutoFocusChannels().forEach(all -> registry.focusChannel(online, all));
                    });

                    Language.localeChat(player, "PLUGIN_RELOADED", null);
                } else Language.localeChat(player, "INVALID_USAGE", null);
            }
        }

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
