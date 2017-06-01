package com.codenameflip.chatchannels.listeners;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.channels.Channel;
import com.codenameflip.chatchannels.events.ChannelChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author codenameflip
 * @since 4/7/17
 */

public class PlayerChat implements Listener {

    private List<UUID> onCooldown = new ArrayList<>();
    private List<UUID> recentlyWarnedOfChatRadius = new ArrayList<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        String message = event.getMessage();

        event.setCancelled(true); // Cancel the default chat handling; ChatChannels will handle this on it's own.

        if (ChatChannels.get().getFocusedChannel(player) != null)
        {
            Channel channel = ChatChannels.get().getFocusedChannel(player);

            // Make sure the channel is not muted
            if (channel.getPermission().equalsIgnoreCase("chatchannels.bypass-mute") && !player.hasPermission("chatchannels.bypass-mute"))
            {
                player.sendMessage(ChatColor.RED + "This channel has been muted by a staff member, only special users may send messages");

                return;
            }

            // Make sure the player has permission to talk in the currently focused channel.
            if (!channel.getPermission().equalsIgnoreCase("*") && !player.hasPermission(channel.getPermission()))
            {
                player.sendMessage(ChatColor.RED + "You do not have permission to send messages to this channel");

                if (ChatChannels.get().getConfig().getBoolean("chat-settings.announce-permissions"))
                    player.sendMessage(ChatColor.DARK_RED + "Required permission: " + ChatColor.DARK_AQUA + channel.getPermission());

                return;
            }

            // Make sure the channel that the player is trying to send messages to is currently being viewed by the sender.
            if (!channel.isViewing(player))
            {
                player.sendMessage(ChatColor.RED + "You are not currently viewing the channel you're chatting in; view has been re-enabled.");

                channel.toggleView(player);
                return;
            }

            // Make sure the player is not on chat cooldown
            if (onCooldown.contains(player.getUniqueId()))
            {
                player.sendMessage(ChatColor.RED + "You are currently on chat cooldown for the " + channel.getColor() + channel.getName().toUpperCase() + ChatColor.RED + " channel");

                return;
            }

            ChannelChatEvent outgoingEvent = new ChannelChatEvent(player, channel, message);
            Bukkit.getPluginManager().callEvent(outgoingEvent);

            if (!outgoingEvent.isCancelled())
            { // If another plugin does not prevent the message from sending, then send to all viewers of the channel.
                // Check if a chat radius is implemented, if so, calculate everyone nearby
                if (channel.getChatRadius() != 0 && channel.getChatRadius() > 0)
                {
                    double radius = channel.getChatRadius();

                    // Get all the players nearby and compose/send a message to them
                    player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius).stream()
                            .filter(entity -> Math.round(entity.getLocation().distance(player.getLocation())) <= radius)
                            .forEach(entity ->
                            {
                                if (entity instanceof Player)
                                {
                                    // Make sure that all entities gathered are players
                                    Player playerEntity = (Player) entity;

                                    if (ChatChannels.get().getViewingChannels(playerEntity).contains(channel))
                                        playerEntity.sendMessage(ChatColor.translateAlternateColorCodes('&', formRadiusedMessage(channel, player, playerEntity, message)));

                                    if (playerEntity.equals(player))
                                    {
                                        // If a player has not been warned recently of the chat radius in effect, let them know after the message is sent
                                        if (!recentlyWarnedOfChatRadius.contains(player.getUniqueId()))
                                        {
                                            player.sendMessage("" + ChatColor.GRAY + ChatColor.ITALIC + "(The " + channel.getColor() + channel.getName() + " " + ChatColor.GRAY + ChatColor.ITALIC + "channel has a " + ChatColor.YELLOW + channel.getChatRadius() + "m " + ChatColor.GRAY + ChatColor.ITALIC + "chat radius. Only people in that radius will receive your message)");

                                            recentlyWarnedOfChatRadius.add(player.getUniqueId());

                                            new BukkitRunnable() {
                                                @Override
                                                public void run()
                                                {
                                                    recentlyWarnedOfChatRadius.remove(player.getUniqueId());
                                                }
                                            }.runTaskLater(ChatChannels.get(), 20 * 60 * 5);
                                        }
                                    }
                                }
                            });
                } else
                {
                    // Compose/send the message to all viewers of the channel
                    channel.getViewing().forEach(viewer ->
                    {
                        Player viewingPlayer = Bukkit.getPlayer(viewer);

                        if (viewingPlayer != null && viewingPlayer.isOnline())
                            viewingPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', formatMessage(channel, player, message)));
                    });
                }

                // Player gets added to the cooldown after the message(s) is/are sent
                onCooldown.add(player.getUniqueId());

                new BukkitRunnable() {
                    @Override
                    public void run()
                    {
                        // Remove the player from the chat cooldown after the set amount of time
                        onCooldown.remove(player.getUniqueId());
                    }
                }.runTaskLater(ChatChannels.get(), (long) (20 * channel.getCooldown()));
            }
        } else
        {
            player.sendMessage(ChatColor.RED + "You are not currently focused on a chat channel! What is this tomfoolery!?");
            player.sendMessage(ChatColor.GRAY + "(Relogging should resolve this issue; if not please contact a server administrator)");
        }
    }

    /**
     * Forms a chat message using the format specified in the chat settings
     *
     * @param channel The channel the message will be going to
     * @param player  The player sending the message
     * @param message The message the player is sending
     * @return The formatted chat message
     */
    private String formatMessage(Channel channel, Player player, String message)
    {
        String base = ChatChannels.get().getConfig().getString("chat-settings.format");

        base = base.replace("(COLOR)", channel.getColor());
        base = base.replace("(IDENTIFIER)", channel.getIdentifier());
        base = base.replace("(CHANNEL)", channel.getName());
        base = base.replace("(PLAYER)", player.getDisplayName());
        base = base.replace("(MESSAGE)", channel.getChatColor() + message);

        if (ChatChannels.get().isPlaceholderApiInstalled())
        {
            base = PlaceholderAPI.setPlaceholders(player, base);
        }

        return base;
    }

    /**
     * Appends a distance indicator to the formatted message
     *
     * @param channel The channel the message will be going to
     * @param sender  The player sending the message
     * @param compare The player who will be receiving the message
     * @param message The message the player is sending
     * @return The formatted chat message with the distance indicator
     */
    private String formRadiusedMessage(Channel channel, Player sender, Player compare, String message)
    {
        if (!sender.equals(compare))
            return "" + ChatColor.GRAY + ChatColor.ITALIC + "(" + Math.round(sender.getEyeLocation().distance(compare.getEyeLocation())) + "m) " + formatMessage(channel, sender, message);
        else
            return formatMessage(channel, sender, message);
    }

}
