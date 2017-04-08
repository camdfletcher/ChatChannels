package com.codenameflip.chatchannels.commands;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.channels.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author codenameflip
 * @since 4/8/17
 */

public class ChatAdminCmd implements CommandExecutor {

    private Map<Channel, String> storedPermissionData = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("chatAdmin") && sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("chatchannels.cmd.chatadmin")) {
                if (args.length < 1) {
                    Stream.of(
                            "" + ChatColor.RED + ChatColor.BOLD + "Chat Commands " + ChatColor.WHITE + ChatColor.STRIKETHROUGH + "--" + ChatColor.RESET + " " + ChatColor.GRAY + "Enter a sub command...",
                            "" + ChatColor.RED + "/chatAdmin mute (channel)" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Halts all communication in chat",
                            "" + ChatColor.RED + "/chatAdmin clear (channel)" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Clears a channel's chat",
                            "" + ChatColor.RED + "/chatAdmin channelData" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Dumps all channel data in chat",
                            "" + ChatColor.RED + "/chatAdmin reload" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Reloads the ChatChannels config.yml"
                    ).forEach(player::sendMessage);
                } else {
                    // Handle subcommands

                    if (args[0].equalsIgnoreCase("mute")) {
                        if (args.length == 2) {

                            Channel targetChannel = ChatChannels.get().getChannel(args[1]);
                            final String MUTE_PERMISSION = "chatchannels.bypass-mute";

                            if (targetChannel == null) {
                                player.sendMessage(ChatColor.RED + "Invalid channel specified; did you spell it correctly?");

                                return false;
                            }

                            if (targetChannel.getPermission().equalsIgnoreCase(MUTE_PERMISSION)) {
                                // If the channel is already muted then reset the permission

                                targetChannel.setPermission(storedPermissionData.get(targetChannel));
                                storedPermissionData.remove(targetChannel);

                                player.sendMessage(ChatColor.DARK_AQUA + "You have " + ChatColor.RED + ChatColor.BOLD + "UNMUTED" + ChatColor.DARK_AQUA + " the [" + targetChannel.getName() + "] channel.");

                                System.out.println(" ** [CHAT LOG] (" + targetChannel.getName() + ") (!!!) Channel was unmuted by " + player.getName() + "(!!!)");
                            } else {
                                // If the channel is NOT muted, set the temp. permission

                                storedPermissionData.put(targetChannel, targetChannel.getPermission());
                                targetChannel.setPermission("chatchannels.bypass-mute");

                                player.sendMessage(ChatColor.DARK_AQUA + "You have " + ChatColor.RED + ChatColor.BOLD + "MUTED" + ChatColor.DARK_AQUA + " the [" + targetChannel.getName() + "] channel.");

                                System.out.println(" ** [CHAT LOG] (" + targetChannel.getName() + ") (!!!) Channel was muted by " + player.getName() + "(!!!)");
                            }

                        } else {
                            player.sendMessage(ChatColor.RED + "Invalid command usage.");
                        }
                    } else if (args[0].equalsIgnoreCase("clear")) {
                        if (args.length == 2) {

                            Channel targetChannel = ChatChannels.get().getChannel(args[1]);

                            if (targetChannel == null) {
                                player.sendMessage(ChatColor.RED + "Invalid channel specified; did you spell it correctly?");

                                return false;
                            }

                            targetChannel.getViewing().forEach(uuid -> {
                                Player viewer = Bukkit.getPlayer(uuid);

                                if (viewer != null && viewer.isOnline()) {
                                    for (int i = 0; i < 1000; i++) {
                                        viewer.sendMessage(" ");
                                    }

                                    viewer.sendMessage(" " + ChatColor.RED + ChatColor.BOLD + "> Channel chat has been cleared by " + ChatColor.AQUA + ChatColor.BOLD + player.getName() + ChatColor.RED + ChatColor.BOLD + " <");
                                }
                            });

                            System.out.println(" ** [CHAT LOG] (" + targetChannel.getName() + ") (!!!) Channel was cleared by " + player.getName() + " (!!!)");

                        } else {
                            player.sendMessage(ChatColor.RED + "Invalid command usage.");
                        }
                    } else if (args[0].equalsIgnoreCase("channelData")) {

                        for (Channel channel : ChatChannels.get().getChannels()) {
                            Stream.of(
                                    "" + channel.getColor() + ChatColor.BOLD + channel.getName() + ChatColor.GRAY + " (" + channel.getDescription() + ")",
                                    "" + ChatColor.WHITE + "Identifier: " + ChatColor.GREEN + channel.getColor() + channel.getIdentifier(),
                                    "" + ChatColor.WHITE + "Default Focus? " + ChatColor.GREEN + channel.isFocusByDefault(),
                                    "" + ChatColor.WHITE + "Default View? " + ChatColor.GREEN + channel.isViewByDefault(),
                                    "" + ChatColor.WHITE + "Permission:  " + ChatColor.GREEN + channel.getPermission(),
                                    "" + ChatColor.WHITE + "Chat Cooldown: " + ChatColor.GREEN + channel.getCooldown() + " seconds",
                                    "" + ChatColor.WHITE + "Color of Chat: " + channel.getChatColor() + "Example chat message",
                                    "" + ChatColor.WHITE + "Color: " + channel.getColor() + "Color",
                                    " "
                            ).forEach(player::sendMessage);
                        }

                        player.sendMessage(ChatColor.RED + "End of dump data.");
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        ChatChannels.get().reloadConfig();

                        player.sendMessage(ChatColor.YELLOW + "Reloaded configuration file!");
                    }
                }
            }
        }

        return false;
    }

}
