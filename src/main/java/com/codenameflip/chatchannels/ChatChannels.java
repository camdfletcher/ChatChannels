package com.codenameflip.chatchannels;

import com.codenameflip.chatchannels.channels.Channel;
import com.codenameflip.chatchannels.commands.ChatAdminCmd;
import com.codenameflip.chatchannels.commands.ChatCmd;
import com.codenameflip.chatchannels.listeners.ChannelChat;
import com.codenameflip.chatchannels.listeners.PlayerChat;
import com.codenameflip.chatchannels.listeners.PlayerJoin;
import com.codenameflip.chatchannels.updater.UpdateHandler;
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

	private static final String PLUGIN_RESOURCE_ID = "39100";
	private static final String PLUGIN_RESOURCE_URL =
			"https://api.spiget.org/v2/resources/%ID%/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels";
	private static final String PLUGIN_VERSION_URL =
			"https://api.spiget.org/v2/resources/%ID%/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels";
    
	private static ChatChannels instance;

	private Set<Channel> channels = new HashSet<>();
	private Set<Channel> defaultViewingChannels = new HashSet<>();
	private Channel defaultFocusChannel = null;

	private UpdateHandler updateHandler;

	public static ChatChannels get()
	{
		return ChatChannels.instance;
	}

	@Override
	public void onEnable()
	{
		instance = this;

		if (!(new File(this.getDataFolder(), "config.yml")).exists())
		{
			this.saveDefaultConfig();
		}

		this.registerChannels();

		Stream.of(
				new PlayerChat(),
				new PlayerJoin(),
				new ChannelChat()
		).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

		this.getCommand("chat").setExecutor(new ChatCmd());
		this.getCommand("chatAdmin").setExecutor(new ChatAdminCmd());

		this.updateHandler = UpdateHandler.builder()
				.setResourceId(ChatChannels.PLUGIN_RESOURCE_ID)
				.setDescriptionUrl(ChatChannels.PLUGIN_RESOURCE_URL)
				.setVersionUrl(ChatChannels.PLUGIN_VERSION_URL)
				.build();

		// If a reload happens, automatically focus/view default channels for players (prevents the need for relogging to review/focus)
		Bukkit.getOnlinePlayers().forEach(player ->
		{
			this.getDefaultViewingChannels().forEach(channel -> channel.display(player));
			this.getDefaultChannel().focus(player);
		});
	}

	@Override
	public void onDisable()
	{

	}

	public boolean isPlaceholderApiInstalled()
	{
		return this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
	}

	private void registerChannels()
	{
		ConfigurationSection channelsList = this.getConfig().getConfigurationSection("channels");

		for (String key : channelsList.getKeys(false))
		{
			System.out.println(" * Now loading '" + key + "'...");

			String name = getConfig().getString("channels." + key + ".name");
			String description = getConfig().getString("channels." + key + ".description");
			String identifier = getConfig().getString("channels." + key + ".identifier");
			String permission = getConfig().getString("channels." + key + ".permission");
			boolean automaticFocus = getConfig().getBoolean("channels." + key + ".automatically-focus");
			boolean automaticView = getConfig().getBoolean("channels." + key + ".automatically-view");
			double cooldown = getConfig().getDouble("channels." + key + ".cooldown");
			double chatRadius = getConfig().getDouble("channels." + key + ".chat-radius");
			String color = getConfig().getString("channels." + key + ".color");
			String chatColor = getConfig().getString("channels." + key + ".chat-color");

			Channel incomingChannel = new Channel(identifier, name, description);

			incomingChannel.setPermission(permission);
			incomingChannel.setColor(color);
			incomingChannel.setChatColor(chatColor);
			incomingChannel.setCooldown(cooldown);
			incomingChannel.setChatRadius(chatRadius);
			incomingChannel.setFocusByDefault(automaticFocus);
			incomingChannel.setViewByDefault(automaticView);

			incomingChannel.register();
		}

		System.out.println(" *** All configuration channels registered!");
	}

	public Set<Channel> getChannels()
	{
		return new HashSet<>(this.channels);
	}

	/**
	 * Gets a Channel object instance via name
	 *
	 * @param name The name of the channel you would like to get
	 * @return The instance of the stored Channel (if it exists)
	 */
	public Channel getChannel(String name)
	{
		return this.getChannels().stream()
				.filter(entries -> entries.getName().equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Gets the channel a player is currently focused on
	 *
	 * @param player The player who's focused channel you would like to get
	 * @return The channel the player is currently focused on (if exists)
	 */
	public Channel getFocusedChannel(Player player)
	{
		return this.getChannels().stream()
				.filter(entries -> entries.isFocused(player))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Gets a list of all channels a player is currently viewing
	 *
	 * @param player The player who's viewing channels you would like to get
	 * @return An array of Channel objects which have stored the player as viewing
	 */
	public List<Channel> getViewingChannels(Player player)
	{
		return this.getChannels().stream()
				.filter(entries -> entries.isViewing(player))
				.collect(Collectors.toList());
	}

	/**
	 * Gets the default focus channel specified in the configuration file
	 *
	 * @return The default focus channel
	 */
	public Channel getDefaultChannel()
	{
		return this.defaultFocusChannel;
	}

	/**
	 * Gets the array of Channels which players will automatically be forced to see when they join the server (specified in the configuration file)
	 *
	 * @return The list of Channels
	 */
	public Set<Channel> getDefaultViewingChannels()
	{
		return this.defaultViewingChannels;
	}

	/**
	 * Gets the UpdateHandler instance for the plugin
	 *
	 * @return UpdateHandler instance
	 */
	public UpdateHandler getUpdateHandler()
	{
		return this.updateHandler;
	}
	
}