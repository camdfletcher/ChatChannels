package com.codenameflip.chatchannels.utils;

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

    private Language() {
    }

    private static final HashMap<String, String> TEXT = new HashMap<>();

    static {
        // Console Output
        TEXT.put("ENABLED", "&bChatChannels has been successfully enabled!");
        TEXT.put("DISABLED", "&cChatChannels has been successfully disabled!");
        TEXT.put("CONSTRUCT_REGISTRY", "&aRegistry construction started... Using '%registry%'...");
        TEXT.put("DECONSTRUCT_REGISTRY", "&cRegistry deconstruction started... Goodbye...");
        TEXT.put("LOADED_CHANNEL", "&eSuccessfully loaded channel %channel%!");

        // Player Commands
        TEXT.put("CHANNEL_SHOW", "&6&l[Chat] &aYou are now viewing the %color%&l%name% &achannel.");
        TEXT.put("CHANNEL_HIDE", "&6&l[Chat] &eYou have hidden the %color%&l%name% &7channel.");
        TEXT.put("CHANNEL_FOCUS", "&6&l[Chat] &bYou are now focused on the %color%&l%name% &bchannel.");

        // Moderation
        TEXT.put("CHAT_CLEARED", "    &d&l*** CHANNEL CLEARED BY &e&l%executor% &d&l***");
        TEXT.put("CHAT_MUTED", "    &c&l* CHANNEL MUTED BY &e&l%executor% &c&l*");
        TEXT.put("CHAT_UNMUTED", "    &a&l* CHANNEL UNMUTED BY &e&l%executor% &a&l*");
        TEXT.put("CANNOT_CHAT", "&6&l[Chat] &cChatting in this channel has been temporarily prohibited by staff.");
        TEXT.put("PLUGIN_RELOADED", "&6&l[ChatChannels] &aPlugin reloaded!");

        // Generic
        TEXT.put("INVALID_PARAM", "&cInvalid value for parameter '%param%'");
        TEXT.put("INVALID_OPERATION", "&6&l[Chat] &c&o%reason%");
        TEXT.put("INVALID_USAGE", "&cInvalid command usage.");
        TEXT.put("NO_PERMS", "&6&l[Chat] &cYou do not have permission to interact with this channel!");
        TEXT.put("NO_PERMS_EXACT", "&3You're lacking the permission node: &f%permission%");
        TEXT.put("NOT_VIEWING", "&cYou weren't viewing the focused channel! Toggling...");
        TEXT.put("ON_COOLDOWN", "&cYou're currently on cooldown and cannot chat in this channel!");
    }

    /**
     * Sends a message to a {@link Player} using a predefined message
     *
     * @param player       The {@link Player} who you would like to send the message to
     * @param message      The identifier for the message you would like to send
     * @param placeholders A map of placeholders and replacements
     */
    public static void localeChat(Player player, String message, HashMap<String, Object> placeholders) {
        player.sendMessage(parse(message, placeholders));
    }

    /**
     * Sends a message to console using a predefined message
     *
     * @param message      The identifier for the message you would like to send
     * @param placeholders A map of placeholders and replacements
     */
    public static void localeConsole(String message, HashMap<String, Object> placeholders) {
        Bukkit.getConsoleSender().sendMessage(parse(message, placeholders)); // using #getConsoleSender() to allow for colors
    }

    /**
     * Parses a text identifier and substitutes placeholders
     *
     * @param message      The identifier for the message you would like to parse
     * @param placeholders The map of placeholders that should be used to substitute
     * @return A formatted message
     */
    private static String parse(String message, HashMap<String, Object> placeholders) {
        if (!TEXT.containsKey(message))
            throw new RuntimeException("Attempted to locale invalid message '" + message + "'");

        if (placeholders == null) return color(TEXT.get(message));

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
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
