package com.codenameflip.chatchannels.structure;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.utils.Language;
import com.codenameflip.chatchannels.utils.Placeholders;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/4/18
 */
public class SimpleChannelRegistry extends IChannelRegistry {

    private final HashSet<Channel> CHANNELS = new HashSet<>();

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

            Language.localeConsole("LOADED_CHANNEL", new Placeholders().put("channel", channel.getIdentifier()).build());
        }
    }

    @Override
    public void deconstruct() {
        Language.localeConsole("DECONSTRUCT_REGISTRY", null);
        CHANNELS.clear();
    }

    @Override
    public Set<Channel> getChannels() {
        return CHANNELS;
    }

    @Override
    public Optional<Channel> getChannel(String identifier) {
        return CHANNELS.stream()
                .filter(channel -> channel.getIdentifier().equalsIgnoreCase(identifier))
                .findAny();
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

}
