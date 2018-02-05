package com.codenameflip.chatchannels;

import com.codenameflip.chatchannels.structure.IChannelRegistry;
import com.codenameflip.chatchannels.structure.SimpleChannelRegistry;
import com.codenameflip.chatchannels.utils.Language;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/3/18
 */
public final class ChatChannels extends JavaPlugin {

    private static final String
            PLUGIN_RESOURCE_ID = "39100",
            PLUGIN_RESOURCE_URL = "https://api.spiget.org/v2/resources/%ID%/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels",
            PLUGIN_VERSION_URL = "https://api.spiget.org/v2/resources/%ID%/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels";

    @Getter
    private static ChatChannels instance;

    @Getter
    private IChannelRegistry registry;

    @Override
    public void onEnable()
    {
        instance = this;
        this.saveDefaultConfig();

        registry = new SimpleChannelRegistry();
        registry.construct();

        Language.localeConsole("ENABLED", null);
    }

    @Override
    public void onDisable()
    {
        registry.deconstruct();

        Language.localeConsole("DISABLED", null);
    }

}