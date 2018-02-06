package com.codenameflip.chatchannels.structure;

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
    public final String identifier;

    /**
     * The registry aliases, also used for internal identification and in the configuration file
     */
    @Getter
    public final List<String> aliases;

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
     * @return
     */
    abstract public Set<Channel> getChannels();

    /**
     * Retrieves an {@link Optional<Channel>} using a string identifier
     *
     * @param identifier The identifier of the target channel
     * @return {@link Optional<Channel>}, if present then a channel could be found, otherwise it couldn't
     */
    abstract public Optional<Channel> getChannel(String identifier);

    /**
     * Wrapper function, see below.
     */
    public void focusChannel(Player player, Channel channel)
    {
        focusChannel(player.getUniqueId(), channel);
    }

    /**
     * Wrapper function, see below.
     */
    public void showChannel(Player player, Channel channel)
    {
        showChannel(player.getUniqueId(), channel);
    }

    /**
     * Wrapper function, see below.
     */
    public void hideChannel(Player player, Channel channel)
    {
        hideChannel(player.getUniqueId(), channel);
    }

    /**
     * Focuses a player's chat into a specified {@link Channel}
     *
     * @param uuid The {@link UUID} of the target player
     * @param channel The {@link Channel} you would like to have the player focused on
     */
    public void focusChannel(UUID uuid, Channel channel)
    {
        if (channel.getFocusedPlayers().contains(uuid)) return; // Already focused

        channel.getFocusedPlayers().add(uuid);
        attemptLocale(uuid, "CHANNEL_FOCUS", getFormatting(channel));
    }

    /**
     * Displays a specific channel to a player
     *
     * @param uuid The {@link UUID} of the target player
     * @param channel The {@link Channel} you would like shown
     */
    public void showChannel(UUID uuid, Channel channel)
    {
        if (!channel.getHiddenPlayers().contains(uuid)) return; // Already shown

        channel.getHiddenPlayers().remove(uuid);
        attemptLocale(uuid, "CHANNEL_SHOW", getFormatting(channel));
    }

    /**
     * Hides a specific channel from a player
     *
     * @param uuid The {@link UUID} of the target player
     * @param channel The {@link Channel} you would like hidden
     */
    public void hideChannel(UUID uuid, Channel channel)
    {
        if (channel.getHiddenPlayers().contains(uuid)) return; // Already hidden

        channel.getHiddenPlayers().add(uuid);
        attemptLocale(uuid, "CHANNEL_HIDE", getFormatting(channel));
    }

    /**
     * Utility method that generates placeholders from a {@link Channel}'s {@link ChannelProperties}
     *
     * @param channel The {@link Channel} that formatting is being generated for
     * @return A {@link HashMap} with placeholder replacements that are used when localing the chat
     */
    private HashMap<String, Object> getFormatting(Channel channel)
    {
        return new Placeholders()
                .put("color", channel.getProperties().getColor())
                .put("name", channel.getDisplayName())
                .build();
    }

    /**
     * Safely handles localing with {@link UUID}
     *
     * WARNING: This method will abort if the player cannot be found.
     *
     * @param uuid The {@link UUID} you're trying to message
     * @param message The message identifier you're trying to send
     * @param formatting The placeholder options that will be used when localing the message
     */
    private void attemptLocale(UUID uuid, String message, HashMap<String, Object> formatting)
    {
        Player target = Bukkit.getPlayer(uuid);

        if (target == null) return; // Unable to locale the chat, abort.

        Language.localeChat(target, message, formatting);
    }

}
