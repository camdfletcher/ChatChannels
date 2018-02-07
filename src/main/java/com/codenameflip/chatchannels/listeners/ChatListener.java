package com.codenameflip.chatchannels.listeners;

import com.codenameflip.chatchannels.events.ChannelChatEvent;
import com.codenameflip.chatchannels.events.ChannelPreChatEvent;
import com.codenameflip.chatchannels.structure.Channel;
import com.codenameflip.chatchannels.structure.ChannelProperties;
import com.codenameflip.chatchannels.utils.Cooldowns;
import com.codenameflip.chatchannels.utils.Language;
import com.codenameflip.chatchannels.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 02/07/2018
 */
public class ChatListener implements ChatChannelsListener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (getRegistry() == null) return; // If chat channels couldn't successfully make a registry instance then handle chat vanilla.
        else event.setCancelled(true);

        Optional<Channel> channel = getRegistry().getFocusedChannel(player.getUniqueId());

        if (!channel.isPresent()) {
            getRegistry().getChannels().stream()
                    .filter(all -> all.getProperties().isShowByDefault())
                    .forEach(all -> {
                        getRegistry().showChannel(player, all);
                        if (all.getProperties().isFocusByDefault())  getRegistry().focusChannel(player, all);
                    });
        }

        channel = getRegistry().getFocusedChannel(player.getUniqueId()); // Update the channel, just in case it was null before and is now set.

        // Handle mute management
        if (channel.get().getProperties().isMuted()) {
            if (!player.hasPermission("chatchannels.bypass-mute")) Language.localeChat(player, "CANNOT_CHAT", null);
            return;
        }

        // Handle permission management
        if (!channel.get().getProperties().getPermission().equalsIgnoreCase("*")
                && !player.hasPermission(channel.get().getProperties().getPermission())) {
            Language.localeChat(player, "NO_PERMS", null);

            if (get().getConfig().getBoolean("chat-settings.announce-permissions"))
                Language.localeChat(player, "NO_PERMS_EXACT", new Placeholders("permission", channel.get().getProperties().getPermission()).build());

            return;
        }

        // Confirm they're viewing the channel being chatted in
        if (!channel.get().getViewingPlayers().contains(player.getUniqueId())) {
            Language.localeChat(player, "NOT_VIEWING", null);
            getRegistry().showChannel(player, channel.get());
        }

        ChannelPreChatEvent preChatEvent = new ChannelPreChatEvent(player, channel.get(), message);
        Bukkit.getPluginManager().callEvent(preChatEvent);

        // Make sure the event isn't being hijacked and cancelled elsewhere
        if (!preChatEvent.isCancelled()) {
            String cooldownId = "COOLDOWN_" + channel.get().getIdentifier();

            if (!Cooldowns.isDone(player, cooldownId)) {
                Language.localeChat(player, "ON_COOLDOWN", null);
                return;
            }

            // TODO: Radius management

            ChannelChatEvent chatEvent = new ChannelChatEvent(player, channel.get(), message);
            Bukkit.getPluginManager().callEvent(chatEvent);

            channel.get().broadcastRaw(format(player, channel.get(), message));

            if (channel.get().getProperties().getCooldown() > 0) {
                Cooldowns.on(player)
                        .forTime(TimeUnit.SECONDS.toMillis((long) channel.get().getProperties().getCooldown()))
                        .forReason("COOLDOWN_" + channel.get().getIdentifier())
                        .done();
            }
        }
    }

    /**
     * Utility message to make formatting channel messages easier
     *
     * @param player The {@link Player} sending the message
     * @param channel The {@link Channel} the message is being sent in
     * @param message The message that is being sent
     * @return A formatted version on the message using the format specified in the configuration file
     */
    private String format(Player player, Channel channel, String message) {
        // Bundled Placeholders:
        // - %color%        ::  The color specified in focused channel's properties  (The channel color)
        // - %chatcolor%    ::  The color specified in focused channel's properties  (The color on the chat)
        // - %identifier%   ::  The identifier specified in the focused channel's properties
        // - %channel%      ::  The name on the channel specified in the focused channel's properties
        // - %player%       ::  The name on the player sending the message
        // - %message%      ::  The message being sent

        String format = get().getConfig().getString("chat-settings.format");
        ChannelProperties properties = channel.getProperties();

        format = format.replaceAll("%color%", properties.getColor());
        format = format.replaceAll("%chatcolor%", properties.getChatColor());
        format = format.replaceAll("%identifier%", channel.getIdentifier());
        format = format.replaceAll("%channel%", channel.getDisplayName());
        format = format.replaceAll("%player%", player.getName());
        format = format.replaceAll("%message%", message);

        return Language.color(format); // Just in-case.
    }

}
