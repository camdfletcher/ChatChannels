package com.codenameflip.chatchannels.channels;

import com.codenameflip.chatchannels.ChatChannels;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author codenameflip
 * @since 4/6/17
 */

public class Channel {

    private String identifier;
    private String name;
    private String description;
    private String permission = "*";
    private String color = "§f";
    private String chatColor = "§7";
    private double cooldown = 0;
    private double chatRadius = 0;
    private boolean viewByDefault = true;
    private boolean focusByDefault = true;
    private Set<UUID> viewing = new HashSet<>();
    private Set<UUID> focused = new HashSet<>();

    /**
     * The fundamental object used to store data regarding chat channels in the plugin
     *
     * @param identifier    The string used to identify the channel (in configurations and commands, ex: "announcements", "staff_channel")
     * @param name          The formatted name of the channel (ex: "Announcements", "Staff Channel")
     * @param description   The description of the channel's purpose
     */
    public Channel(String identifier, String name, String description)
    {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public Set<UUID> getViewing()
    {
        return viewing;
    }

    public Set<UUID> getFocused()
    {
        return focused;
    }

    public String getPermission()
    {
        return permission;
    }

    public void setPermission(String permission)
    {
        this.permission = permission;
    }

    public String getColor()
    {
        return ChatColor.translateAlternateColorCodes('&', color);
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getChatColor()
    {
        return ChatColor.translateAlternateColorCodes('&', chatColor);
    }

    public void setChatColor(String chatColor)
    {
        this.chatColor = chatColor;
    }

    public double getCooldown()
    {
        return cooldown;
    }

    public void setCooldown(double cooldown)
    {
        this.cooldown = cooldown;
    }

    public double getChatRadius()
    {
        return chatRadius;
    }

    public void setChatRadius(double chatRadius)
    {
        this.chatRadius = chatRadius;
    }

    public boolean isViewByDefault()
    {
        return viewByDefault;
    }

    public void setViewByDefault(boolean viewByDefault)
    {
        this.viewByDefault = viewByDefault;
    }

    public boolean isFocusByDefault()
    {
        return focusByDefault;
    }

    public void setFocusByDefault(boolean focusByDefault)
    {
        this.focusByDefault = focusByDefault;
    }

    public boolean isViewing(Player player)
    {
        return viewing.contains(player.getUniqueId());
    }

    public boolean isFocused(Player player)
    {
        return focused.contains(player.getUniqueId());
    }

    /**
     * Forces a player to "focus" or begin talking in this channel
     *
     * @param player The player who you would like to focus to this channel
     */
    public void focus(Player player)
    {
        focus(player, true);
    }

    /**
     * Forces a player to "focus" or begin talking in this channel
     *
     * @param player The player who you would like to focus to this channel
     * @param notify Whether or not to notify the player of this change
     */
    public void focus(Player player, boolean notify)
    {
        ChatChannels.get().getChannels().forEach(channel ->
        {
            if (channel.getFocused().contains(player.getUniqueId()))
                channel.getFocused().remove(player.getUniqueId());
        });

        focused.add(player.getUniqueId());

        if (notify)
            player.sendMessage(ChatColor.AQUA + "Set your chat focus to " + getColor() + ChatColor.BOLD + "[" + getName() + "]");
    }

    /**
     * Forces a player to view this channel
     *
     * @param player The player who you would like to view this channel
     */
    public void display(Player player)
    {
        display(player, true);
    }

    /**
     * Forces a player to view this channel
     *
     * @param player The player who you would like to view this channel
     * @param notify Whether or not to notify the player of this change
     */
    public void display(Player player, boolean notify)
    {
        if (!viewing.contains(player.getUniqueId()))
        {
            viewing.add(player.getUniqueId());

            if (notify)
                player.sendMessage(ChatColor.GREEN + "You are now viewing " + getColor() + ChatColor.BOLD + "[" + getName() + "]");

        }
    }

    /**
     * Forces a player to hide this channel
     *
     * @param player The player who you would like to hide this channel from
     */
    public void hide(Player player)
    {
        if (viewing.contains(player.getUniqueId()))
        {
            viewing.remove(player.getUniqueId());

            player.sendMessage(ChatColor.GREEN + "You are no longer viewing " + getColor() + ChatColor.BOLD + "[" + getName() + "]");
        }
    }

    /**
     * Utility method
     * Toggles a player's view of this channel, either on or off.
     *
     * @param player The player who's view you would like to toggle
     */
    public void toggleView(Player player)
    {
        if (isViewing(player))
            hide(player);
        else
            display(player);
    }

    /**
     * Registers a Channel object to memory in the main class
     */
    public void register()
    {
        ChatChannels.get().getChannels().add(this);

        System.out.println(" * New chat channel registered (" + getName() + ")...");
    }
}
