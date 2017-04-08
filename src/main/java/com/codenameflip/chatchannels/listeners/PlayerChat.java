package com.codenameflip.chatchannels.listeners;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.channels.Channel;
import com.codenameflip.chatchannels.events.ChannelChatEvent;
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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        event.setCancelled(true); // Cancel the default chat handling; ChatChannels will handle this on it's own.

        if (ChatChannels.get().getFocusedChannel(player) != null) {
            Channel channel = ChatChannels.get().getFocusedChannel(player);

            // Make sure the channel is not muted
            if (channel.getPermission().equalsIgnoreCase("chatchannels.bypass-mute") && !player.hasPermission("chatchannels.bypass-mute")) {
                player.sendMessage(ChatColor.RED + "This channel has been muted by a staff member, only special users may send messages");

                return;
            }

            // Make sure the player has permission to talk in the currently focused channel.
            if (!channel.getPermission().equalsIgnoreCase("*") && !player.hasPermission(channel.getPermission())) {
                player.sendMessage(ChatColor.RED + "You do not have permission to send messages to this channel");

                if (ChatChannels.get().getConfig().getBoolean("chat-settings.announce-permissions"))
                    player.sendMessage(ChatColor.DARK_RED + "Required permission: " + ChatColor.DARK_AQUA + channel.getPermission());

                return;
            }

            // Make sure the channel that the player is trying to send messages to is currently being viewed by the sender.
            if (!channel.isViewing(player)) {
                player.sendMessage(ChatColor.RED + "You are not currently viewing the channel you're chatting in; view has been re-enabled.");

                channel.toggleView(player);
                return;
            }

            // Make sure the player is not on chat cooldown
            if (onCooldown.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You are currently on chat cooldown for the " + channel.getColor() + channel.getName().toUpperCase() + ChatColor.RED + " channel");

                return;
            }

            ChannelChatEvent outgoingEvent = new ChannelChatEvent(player, channel, message);
            Bukkit.getPluginManager().callEvent(outgoingEvent);

            if (!outgoingEvent.isCancelled()) { // If another plugin does not prevent the message from sending, then send to all viewers of the channel.
                // Compose/send the message to all viewers of the channel
                channel.getViewing().forEach(viewer -> {
                    Player viewingPlayer = Bukkit.getPlayer(viewer);

                    if (viewingPlayer != null && viewingPlayer.isOnline())
                        viewingPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', formatMessage(channel, viewingPlayer, message)));
                });

                // Player gets added to the cooldown after the message(s) is/are sent
                onCooldown.add(player.getUniqueId());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Remove the player from the chat cooldown after the set amount of time
                        onCooldown.remove(player.getUniqueId());
                    }
                }.runTaskLater(ChatChannels.get(), (long) (20 * channel.getCooldown()));
            }
        } else {
            player.sendMessage(ChatColor.RED + "You are not currently focused on a chat channel! What is this tomfoolery!?");
            player.sendMessage(ChatColor.GRAY + "(Relogging should resolve this issue; if not please contact a server administrator)");
        }
    }

    private String formatMessage(Channel channel, Player player, String message) {
        String base = ChatChannels.get().getConfig().getString("chat-settings.format");

        base = base.replace("(COLOR)", channel.getColor());
        base = base.replace("(IDENTIFIER)", channel.getIdentifier());
        base = base.replace("(CHANNEL)", channel.getName());
        base = base.replace("(PLAYER)", player.getDisplayName());
        base = base.replace("(MESSAGE)", channel.getChatColor() + message);

        return base;
    }

}
