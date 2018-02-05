package com.codenameflip.chatchannels.channel;

import com.codenameflip.chatchannels.ChatChannels;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/4/18
 */
public class ConfigChannel extends IChannel {

    private final FileConfiguration config;

    public ConfigChannel(String identifier, String displayName)
    {
        super(identifier, displayName);

        config = ChatChannels.get().getConfig();
    }

    // TODO: Pending configuration restructure

    @Override
    public void load()
    {

    }

    @Override
    public void save()
    {

    }
}
