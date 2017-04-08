package com.codenameflip.chatchannels;

import com.codenameflip.chatchannels.channels.Channel;
import com.codenameflip.chatchannels.commands.ChatAdminCmd;
import com.codenameflip.chatchannels.commands.ChatCmd;
import com.codenameflip.chatchannels.listeners.ChannelChat;
import com.codenameflip.chatchannels.listeners.PlayerChat;
import com.codenameflip.chatchannels.listeners.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ChatChannels extends JavaPlugin {

    private static ChatChannels instance;

    private Set<Channel> channels = new HashSet<>();
    private Set<Channel> defaultViewingChannels = new HashSet<>();
    private Channel defaultFocusChannel = null;

    @Override
    public void onEnable() {
        instance = this;

        if (!(new File(getDataFolder(), "config.yml")).exists())
            saveDefaultConfig();

        registerChannels();

        Stream.of(
                new PlayerChat(),
                new PlayerJoin(),
                new ChannelChat()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        getCommand("chat").setExecutor(new ChatCmd());
        getCommand("chatAdmin").setExecutor(new ChatAdminCmd());

        Bukkit.getOnlinePlayers().forEach(player -> {
            getDefaultViewingChannels().forEach(channel -> channel.display(player));
            getDefaultChannel().focus(player);
        });
    }

    @Override
    public void onDisable() {

    }

    private void registerChannels() {
        ConfigurationSection channelsList = getConfig().getConfigurationSection("channels");

        for (String key : channelsList.getKeys(false)) {
            System.out.println("Now loading '" + key + "'...");

            String name = getConfig().getString("channels." + key + ".name");
            String description = getConfig().getString("channels." + key + ".description");
            String identifier = getConfig().getString("channels." + key + ".identifier");
            String permission = getConfig().getString("channels." + key + ".permission");
            boolean automaticFocus = getConfig().getBoolean("channels." + key + ".automatically-focus");
            boolean automaticView = getConfig().getBoolean("channels." + key + ".automatically-view");
            double cooldown = getConfig().getDouble("channels." + key + ".cooldown");
            String color = getConfig().getString("channels." + key + ".color");
            String chatColor = getConfig().getString("channels." + key + ".chat-color");

            Channel incomingChannel = new Channel(identifier, name, description);

            incomingChannel.setPermission(permission);
            incomingChannel.setColor(color);
            incomingChannel.setChatColor(chatColor);
            incomingChannel.setCooldown(cooldown);
            incomingChannel.setFocusByDefault(automaticFocus);
            incomingChannel.setViewByDefault(automaticView);

            incomingChannel.register();

            if (automaticFocus) {
                defaultFocusChannel = incomingChannel;
                System.out.println(" > Detected auto-focus channel");
            }

            if (automaticView) {
                defaultViewingChannels.add(incomingChannel);
                System.out.println(" > Detected auto-view channel");
            }

            System.out.println("** '" + name + "' channel registered.");
        }

        System.out.println(" *** All configuration channels registered!");
    }

    public static ChatChannels get() {
        return instance;
    }

    public Set<Channel> getChannels() {
        return channels;
    }

    public Channel getChannel(String name) {
        return getChannels().stream()
                .filter(entries -> entries.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Channel getFocusedChannel(Player player) {
        return getChannels().stream()
                .filter(entries -> entries.isFocused(player))
                .findFirst()
                .orElse(null);
    }

    public List<Channel> getViewingChannels(Player player) {
        return getChannels().stream()
                .filter(entries -> entries.isViewing(player))
                .collect(Collectors.toList());
    }

    public Channel getDefaultChannel() {
        return defaultFocusChannel;
    }

    public Set<Channel> getDefaultViewingChannels() {
        return defaultViewingChannels;
    }
}