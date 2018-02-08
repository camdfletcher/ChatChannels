package com.codenameflip.chatchannels;

import com.codenameflip.chatchannels.commands.CmdChat;
import com.codenameflip.chatchannels.commands.CmdChatAdmin;
import com.codenameflip.chatchannels.listeners.ChatListener;
import com.codenameflip.chatchannels.listeners.JoinListener;
import com.codenameflip.chatchannels.structure.IChannelRegistry;
import com.codenameflip.chatchannels.structure.SimpleChannelRegistry;
import com.codenameflip.chatchannels.utils.Language;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/3/18
 */
public final class ChatChannels extends JavaPlugin {

    private static final String
            RESOURCE_ID = "39100",
            RESOURCE_URL = "https://api.spiget.org/v2/resources/%ID%/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels",
            VERSION_URL = "https://api.spiget.org/v2/resources/%ID%/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels";

    @Getter
    private static ChatChannels instance;

    @Getter
    private IChannelRegistry registry;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        // Commands
        getCommand("chat").setExecutor(new CmdChat());
        getCommand("chatAdmin").setExecutor(new CmdChatAdmin());

        // Registration/Backend Setup
        if (Arrays.asList("simple", "config", "configuration").contains(getConfig().getString("data.storage-strategy"))) {
            registry = new SimpleChannelRegistry();
            registry.construct();
        }

        // Listeners
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        // Final Touches
        Bukkit.getOnlinePlayers().forEach(player -> {
            registry.getAutoShowChannels().forEach(all -> registry.showChannel(player, all));
            registry.getAutoFocusChannels().forEach(all -> registry.focusChannel(player, all));
        });

        Language.localeConsole("ENABLED", null);
    }

    @Override
    public void onDisable() {
        registry.deconstruct();

        Language.localeConsole("DISABLED", null);
    }

}
