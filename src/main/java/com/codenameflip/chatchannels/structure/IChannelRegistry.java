package com.codenameflip.chatchannels.structure;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.utils.Language;
import com.codenameflip.chatchannels.utils.Placeholders;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/4/18
 */
@RequiredArgsConstructor
public abstract class IChannelRegistry {

    /**
     * The registry identifier, used for internal identification and also in the configuration file
     */
    @Getter
    private final String identifier;

    /**
     * The registry aliases, also used for internal identification and in the configuration file
     */
    @Getter
    private final List<String> aliases;

    /**
     * The equivalent of "onEnable" for the registry
     * <p>
     * Run all loading and initializing here
     */
    abstract public void construct();

    /**
     * The equivalent of "onDisable" for the registry
     * <p>
     * Run all cleanup and disposal here
     */
    abstract public void deconstruct();

    /**
     * A {@link Set} of {@link Channel} that have been registered during registry construction
     *
     * @return {@link Set}
     */
    abstract public Set<Channel> getChannels();

    /**
     * A {@link Set} of {@link Channel} that should be automatically shown when a player joins
     *
     * @return {@link Set}
     */
    abstract public Set<Channel> getAutoShowChannels();

    /**
     * A {@link Set} of {@link Channel} that should be automatically focused when a player joins
     *
     * @return {@link Set}
     */
    abstract public Set<Channel> getAutoFocusChannels();

    /**
     * Retrieves an {@link Optional<Channel>} using a string identifier
     *
     * @param identifier The identifier of the target channel
     * @return {@link Optional<Channel>}, if present then a channel could be found, otherwise it couldn't
     */
    abstract public Optional<Channel> getChannel(String identifier);

    /**
     * Retrieves an {@link Optional<Channel>} object that represents the player's focused channel
     *
     * @param uuid The unique id on the player you'd like to get the channel for
     * @return {@link Optional<Channel>}, if present then a channel could be found, otherwise it couldn't
     */
    abstract public Optional<Channel> getFocusedChannel(UUID uuid);

    /**
     * Handles replacing placeholders and constructing the format for the chat message
     *
     * @param sender The {@link Player} who has sent the message
     * @param message The message being sent
     * @param channel The {@link Channel} the message is being sent to
     * @return A formatted {@link String}
     */
    abstract public String formatMessage(Player sender, String message, Channel channel);

    /**
     * Wrapper function, see below.
     */
    public void focusChannel(Player player, Channel channel) {
        focusChannel(player.getUniqueId(), channel);
    }

    /**
     * Wrapper function, see below.
     */
    public void showChannel(Player player, Channel channel) {
        showChannel(player.getUniqueId(), channel);
    }

    /**
     * Wrapper function, see below.
     */
    public void hideChannel(Player player, Channel channel) {
        hideChannel(player.getUniqueId(), channel);
    }

    /**
     * Focuses a player's chat into a specified {@link Channel}
     *
     * @param uuid    The {@link UUID} of the target player
     * @param channel The {@link Channel} you would like to have the player focused on
     */
    public void focusChannel(UUID uuid, Channel channel) {
        if (channel.getFocusedPlayers().contains(uuid)) {
            attemptLocale(uuid, "INVALID_OPERATION", new Placeholders("reason", "You're already focused on this channel.").build());
            return; // Already focused
        }

        if (!validatePermissions(uuid, channel)) return;

        // Unfocus them from ALL over channels
        getChannels().forEach(all -> all.getFocusedPlayers().remove(uuid));

        channel.getFocusedPlayers().add(uuid);
        attemptLocale(uuid, "CHANNEL_FOCUS", getFormatting(channel));
    }

    /**
     * Displays a specific channel to a player
     *
     * @param uuid    The {@link UUID} of the target player
     * @param channel The {@link Channel} you would like shown
     */
    public void showChannel(UUID uuid, Channel channel) {
        if (channel.getViewingPlayers().contains(uuid)) {
            attemptLocale(uuid, "INVALID_OPERATION", new Placeholders("reason", "You're already viewing this channel.").build());
            return; // Already shown
        }

        if (!validatePermissions(uuid, channel)) return;

        channel.getViewingPlayers().add(uuid);
        attemptLocale(uuid, "CHANNEL_SHOW", getFormatting(channel));
    }

    /**
     * Hides a specific channel from a player
     *
     * @param uuid    The {@link UUID} of the target player
     * @param channel The {@link Channel} you would like hidden
     */
    public void hideChannel(UUID uuid, Channel channel) {
        if (!channel.getViewingPlayers().contains(uuid)) {
            attemptLocale(uuid, "INVALID_OPERATION", new Placeholders("reason", "You've already hidden this channel.").build());
            return; // Already hidden
        }

        if (!validatePermissions(uuid, channel)) return;

        channel.getViewingPlayers().remove(uuid);
        attemptLocale(uuid, "CHANNEL_HIDE", getFormatting(channel));
    }

    /**
     * Utility method that generates placeholders from a {@link Channel}'s {@link ChannelProperties}
     *
     * @param channel The {@link Channel} that formatting is being generated for
     * @return A {@link HashMap} with placeholder replacements that are used when localing the chat
     */
    private HashMap<String, Object> getFormatting(Channel channel) {
        return new Placeholders()
                .put("color", channel.getProperties().getColor())
                .put("name", channel.getDisplayName())
                .build();
    }

    /**
     * Safely handles localing with {@link UUID}
     * <p>
     * WARNING: This method will abort if the player cannot be found.
     *
     * @param uuid       The {@link UUID} you're trying to message
     * @param message    The message identifier you're trying to send
     * @param formatting The placeholder options that will be used when localing the message
     */
    private void attemptLocale(UUID uuid, String message, HashMap<String, Object> formatting) {
        Player target = Bukkit.getPlayer(uuid);

        if (target == null) return; // Unable to locale the chat, abort.

        Language.localeChat(target, message, formatting);
    }

    /**
     * Safely handles permission checking when trying to perform channel operations on users
     *
     * @param uuid The {@link UUID} you'd like to validate permissions for
     * @param channel The {@link Channel} you'd like to use as a comparison for permissions
     * @return Whether the method should continue running due to permissions passing
     */
    private boolean validatePermissions(UUID uuid, Channel channel) {
        Player target = Bukkit.getPlayer(uuid);

        if (target == null) {
            return false;
        }

        if (!channel.getProperties().getPermission().equalsIgnoreCase("*")
                && !target.hasPermission(channel.getProperties().getPermission())) {
            Language.localeChat(target, "NO_PERMS", null);

            if (ChatChannels.getInstance().getConfig().getBoolean("chat-settings.announce-permissions"))
                Language.localeChat(target, "NO_PERMS_EXACT", new Placeholders("permission", channel.getProperties().getPermission()).build());

            return false;
        }

        return true;
    }

}
