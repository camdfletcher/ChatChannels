package com.codenameflip.chatchannels;

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

	private static ChatChannels instance;

	public static ChatChannels get() {
		return ChatChannels.instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
	}

	@Override
	public void onDisable() {
	}

}