package com.codenameflip.chatchannels.utils;

import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/4/18
 */
public class Language {

    private Language()
    {
    }

    private static final HashMap<String, String> TEXT = new HashMap<>();

    static
    {
        // Console
        TEXT.put("ENABLED", "&bChatChannels has been successfully enabled!");
        TEXT.put("DISABLED", "&cChatChannels has been successfully disabled!");
        TEXT.put("CONSTRUCT_REGISTRY", "&aRegistry construction started... Using '%registry%'...");
        TEXT.put("DECONSTRUCT_REGISTRY", "&cRegistry deconstruction started... Goodbye...");
        TEXT.put("LOADED_CHANNEL", "&eSuccessfully loaded channel %channel%!");

        // Player
        TEXT.put("CHANNEL_SHOW", "&aYou are now viewing %color%%name%");
        TEXT.put("CHANNEL_HIDE", "&7You have hidden %color%%name%");
        TEXT.put("CHANNEL_FOCUS", "&bYou are now focused in %color%%name%");
    }

    /**
     * Sends a message to a {@link Player} using a predefined message
     *
     * @param player       The {@link Player} who you would like to send the message to
     * @param message      The identifier for the message you would like to send
     * @param placeholders A map of placeholders and replacements
     */
    public static void localeChat(Player player, String message, @Nullable HashMap<String, Object> placeholders)
    {
        player.sendMessage(parse(message, placeholders));
    }

    /**
     * Sends a message to console using a predefined message
     *
     * @param message      The identifier for the message you would like to send
     * @param placeholders A map of placeholders and replacements
     */
    public static void localeConsole(String message, @Nullable HashMap<String, Object> placeholders)
    {
        Bukkit.getConsoleSender().sendMessage(parse(message, placeholders)); // using #getConsoleSender() to allow for colors
    }

    /**
     * Parses a text identifier and substitutes placeholders
     *
     * @param message      The identifier for the message you would like to parse
     * @param placeholders The map of placeholders that should be used to substitute
     * @return A formatted message
     */
    private static String parse(String message, @Nullable HashMap<String, Object> placeholders)
    {
        if (placeholders == null) return color(message);
        if (!TEXT.containsKey(message))
            throw new RuntimeException("Attempted to locale invalid message '" + message + "'");

        String finalMessage = TEXT.get(message);

        for (Map.Entry<String, Object> entry : placeholders.entrySet())
            finalMessage = finalMessage.replaceAll("%" + entry.getKey() + "%", entry.getValue().toString());

        return color(finalMessage);
    }

    /**
     * Translates '&' color codes into server recognizable color formatting
     *
     * @param string The string you would like to have parsed
     * @return A formatted string
     */
    public static String color(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
