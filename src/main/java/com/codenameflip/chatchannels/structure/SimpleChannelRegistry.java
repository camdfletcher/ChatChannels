package com.codenameflip.chatchannels.structure;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.utils.Language;
import com.codenameflip.chatchannels.utils.Placeholders;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/4/18
 */
public class SimpleChannelRegistry extends IChannelRegistry {

    private final HashSet<Channel> CHANNELS = new HashSet<>();
    private final HashSet<Channel> DEFAULT_SHOW = new HashSet<>();
    private final HashSet<Channel> DEFAULT_FOCUS = new HashSet<>();

    public SimpleChannelRegistry() {
        super("simple", Arrays.asList("config", "configuration"));
    }

    @Override
    public void construct() {
        Language.localeConsole("CONSTRUCT_REGISTRY", new Placeholders().put("registry", getIdentifier()).build());

        FileConfiguration config = ChatChannels.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection("channels");

        for (String key : section.getKeys(false)) {
            String name = str(section, key, "name");
            String identifier = str(section, key, "identifier");
            List<String> aliases = section.getStringList(key + ".aliases");

            ChannelProperties properties = ChannelProperties.builder()
                    .description(str(section, key, "description"))
                    .color(str(section, key, "properties.color"))
                    .chatColor(str(section, key, "properties.chat-color"))
                    .cooldown(dbl(section, key, "properties.cooldown"))
                    .chatRadius(dbl(section, key, "properties.chat-radius"))
                    .permission(str(section, key, "properties.permission"))
                    .showByDefault(bol(section, key, "properties.auto-view"))
                    .focusByDefault(bol(section, key, "properties.auto-focus"))
                    .build();

            Channel channel = new Channel(identifier, name, aliases, properties);
            CHANNELS.add(channel);

            if (properties.isShowByDefault()) DEFAULT_SHOW.add(channel);
            if (properties.isFocusByDefault()) DEFAULT_FOCUS.add(channel);

            Language.localeConsole("LOADED_CHANNEL", new Placeholders("channel", name).build());
        }
    }

    @Override
    public void deconstruct() {
        Language.localeConsole("DECONSTRUCT_REGISTRY", null);
        CHANNELS.clear();
        DEFAULT_FOCUS.clear();
        DEFAULT_SHOW.clear();
    }

    @Override
    public Set<Channel> getChannels() {
        return CHANNELS;
    }

    @Override
    public Set<Channel> getAutoShowChannels() {
        return DEFAULT_SHOW;
    }

    @Override
    public Set<Channel> getAutoFocusChannels() {
        return DEFAULT_FOCUS;
    }

    @Override
    public Optional<Channel> getChannel(String identifier) {
        return CHANNELS.stream()
                .filter(channel -> channel.getIdentifier().equalsIgnoreCase(identifier) || containsEqualsIgnoreCase(channel.getAliases(), identifier))
                .findAny();
    }

    @Override
    public Optional<Channel> getFocusedChannel(UUID uuid) {
        return CHANNELS.stream().
                filter(channel -> channel.getFocusedPlayers().contains(uuid))
                .findFirst();
    }

    @Override
    public String formatMessage(Player sender, String message, Channel channel) {
        String format = ChatChannels.getInstance().getConfig().getString("chat-settings.format");
        ChannelProperties properties = channel.getProperties();

        // Bundled placeholders
        format = format.replaceAll("%color%", properties.getColor());
        format = format.replaceAll("%chatcolor%", properties.getChatColor());
        format = format.replaceAll("%identifier%", channel.getIdentifier());
        format = format.replaceAll("%channel%", channel.getDisplayName());
        format = format.replaceAll("%player%", sender.getDisplayName());
        format = format.replaceAll("%message%", message);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            format = PlaceholderAPI.setPlaceholders(sender, format);

        return Language.color(format); // Just in-case.
    }

    /*
        Utility methods for configuration retrieval
     */

    private String str(ConfigurationSection section, String key, String path) {
        return section.getString(key + "." + path);
    }

    private double dbl(ConfigurationSection section, String key, String path) {
        return section.getDouble(key + "." + path);
    }

    private boolean bol(ConfigurationSection section, String key, String path) {
        return section.getBoolean(key + "." + path);
    }

    private boolean containsEqualsIgnoreCase(List<String> array, String target) {
        for (String s : array) {
            if (s.equalsIgnoreCase(target)) return true;
        }

        return false;
    }

}
