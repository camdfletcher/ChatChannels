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
    }

    public static void localeChat(Player player, String message, @Nullable HashMap<String, Object> placeholders)
    {
        player.sendMessage(parse(message, placeholders));
    }

    public static void localeConsole(String message, @Nullable HashMap<String, Object> placeholders)
    {
        Bukkit.getConsoleSender().sendMessage(parse(message, placeholders)); // using #getConsoleSender() to allow for colors
    }

    public static String parse(String message, @Nullable HashMap<String, Object> placeholders)
    {
        if (placeholders == null) return color(message);
        if (!TEXT.containsKey(message)) throw new RuntimeException("Attempted to locale invalid message '" + message + "'");

        String finalMessage = TEXT.get(message);

        for (Map.Entry<String, Object> entry : placeholders.entrySet())
            finalMessage = finalMessage.replaceAll("%" + entry.getKey() + "%", entry.getValue().toString());

        return color(finalMessage);
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
