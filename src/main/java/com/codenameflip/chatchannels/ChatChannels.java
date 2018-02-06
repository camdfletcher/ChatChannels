package com.codenameflip.chatchannels;

import com.codenameflip.chatchannels.commands.chat.CmdChat;
import com.codenameflip.chatchannels.commands.chat_admin.CmdChatAdmin;
import com.codenameflip.chatchannels.structure.IChannelRegistry;
import com.codenameflip.chatchannels.structure.SimpleChannelRegistry;
import com.codenameflip.chatchannels.utils.Language;
import com.simplexitymc.command.api.CommandHandler;
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
            RESOURCE_ID = "39100",
            RESOURCE_URL = "https://api.spiget.org/v2/resources/%ID%/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels",
            VERSION_URL = "https://api.spiget.org/v2/resources/%ID%/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels";

    @Getter
    private static ChatChannels instance;

    @Getter
    private IChannelRegistry registry;

    @Getter
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        commandHandler = new CommandHandler(this)
                .message(Language.color("&c[ChatChannels] You do not have permission to execute that command!"));
        commandHandler.addCommands(new CmdChat(), new CmdChatAdmin());

        registry = new SimpleChannelRegistry();
        registry.construct();

        Language.localeConsole("ENABLED", null);
    }

    @Override
    public void onDisable() {
        registry.deconstruct();

        Language.localeConsole("DISABLED", null);
    }

}
