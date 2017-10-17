package com.codenameflip.chatchannels.commands;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.channels.Channel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

/**
 * @author codenameflip
 * @since 4/8/17
 */

public class ChatCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (label.equalsIgnoreCase("chat") && sender instanceof Player)
        {
            Player player = (Player) sender;

            if (player.hasPermission("chatchannels.cmd.chat"))
            {
                if (args.length < 1)
                {
                    Stream.of(
                            "" + ChatColor.YELLOW + ChatColor.BOLD + "Chat Commands " + ChatColor.WHITE + ChatColor.STRIKETHROUGH + "--" + ChatColor.RESET + " " + ChatColor.GRAY + "Enter a sub command...",
                            "" + ChatColor.GOLD + "/chat " + ChatColor.BOLD + "f" + ChatColor.GOLD + "ocus (channel)" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Sets your current focus to a channel",
                            "" + ChatColor.GOLD + "/chat " + ChatColor.BOLD + "s" + ChatColor.GOLD + "how (channel)" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Displays a channel",
                            "" + ChatColor.GOLD + "/chat " + ChatColor.BOLD + "h" + ChatColor.GOLD + "ide (channel)" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Hides a channel from view"
                    ).forEach(player::sendMessage);
                }
                else
                {
                    // Handle subcommands

                    if (isSubCommand(args[0], "focus", "f"))
                    {
                        if (args.length == 2)
                        {

                            Channel desiredChannel = ChatChannels.get().getChannel(args[1]);

                            if (desiredChannel == null)
                            {
                                player.sendMessage(ChatColor.RED + "Invalid channel specified; did you spell it correctly?");

                                return false;
                            }

                            if (!desiredChannel.getPermission().equalsIgnoreCase("*"))
                            {
                                if (player.hasPermission(desiredChannel.getPermission()))
                                    desiredChannel.focus(player);
                                else
                                    player.sendMessage(ChatColor.RED + "You do not have permission to focus on the requested channel.");
                            }
                            else
                                desiredChannel.focus(player); // If there is no explicit permission noted for this channel then proceed with whatever action is being taken

                        }
                        else
                        {
                            player.sendMessage(ChatColor.RED + "Invalid command usage.");
                        }
                    }
                    else if (isSubCommand(args[0], "show", "s"))
                    {
                        if (args.length == 2)
                        {

                            Channel desiredChannel = ChatChannels.get().getChannel(args[1]);

                            if (desiredChannel == null)
                            {
                                player.sendMessage(ChatColor.RED + "Invalid channel specified; did you spell it correctly?");

                                return false;
                            }

                            if (!desiredChannel.getPermission().equalsIgnoreCase("*"))
                            {
                                if (player.hasPermission(desiredChannel.getPermission()))
                                    desiredChannel.display(player);
                                else
                                    player.sendMessage(ChatColor.RED + "You do not have permission to display the requested channel.");
                            }
                            else
                                desiredChannel.display(player); // If there is no explicit permission noted for this channel then proceed with whatever action is being taken

                        }
                        else
                        {
                            player.sendMessage(ChatColor.RED + "Invalid command usage.");
                        }
                    }
                    else if (isSubCommand(args[0], "hide", "h"))
                    {
                        if (args.length == 2)
                        {

                            Channel desiredChannel = ChatChannels.get().getChannel(args[1]);

                            if (desiredChannel == null)
                            {
                                player.sendMessage(ChatColor.RED + "Invalid channel specified; did you spell it correctly?");

                                return false;
                            }

                            if (!desiredChannel.getPermission().equalsIgnoreCase("*"))
                            {
                                if (player.hasPermission(desiredChannel.getPermission()))
                                    desiredChannel.hide(player);
                                else
                                    player.sendMessage(ChatColor.RED + "You do not have permission to hide the requested channel.");
                            }
                            else
                                desiredChannel.hide(player); // If there is no explicit permission noted for this channel then proceed with whatever action is being taken

                        }
                        else
                        {
                            player.sendMessage(ChatColor.RED + "Invalid command usage.");
                        }
                    }
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You do not have permission to use the " + ChatColor.GOLD + "/chat " + ChatColor.RED + "command.");
            }
        }

        return false;
    }

    private boolean isSubCommand(String check, String... testFor)
    {
        for (String validSubCommands : testFor)
        {
            if (check.equalsIgnoreCase(validSubCommands))
                return true;
        }

        return false;
    }

}
