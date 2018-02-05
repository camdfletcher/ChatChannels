package com.codenameflip.chatchannels.structure;

import com.codenameflip.chatchannels.utils.Language;
import com.codenameflip.chatchannels.utils.Placeholders;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/3/18
 */
public class ChannelViewer {

    @Getter
    private final Player player;

    /**
     * Simple wrapper to provide access to maintenance methods
     * @param player The player of whom this object will represent
     */
    public ChannelViewer(Player player)
    {
        this.player = player;
    }

    /**
     * The {@link Channel} that the viewer is currently chatting in/is focused on
     */
    @Getter
    public Channel focusedChannel;

    /**
     * A {@link Set} of {@link Channel} objects that the player currently has hidden
     */
    @Getter
    public Set<Channel> hiddenChannels;

    // TODO:

    /**
     * Sets a player's {@link #focusedChannel} channel to another target channel
     * @param channel A {@link Channel} object instance
     */
    public void focus(Channel channel)
    {
        focusedChannel = channel;

        Language.localeChat(player, "CHANNEL_FOCUS", getFormatting(channel));
    }

    /**
     * Shows a specific {@link Channel} to a viewer
     * NOTE: This will only execute if the channel is already hidden to begin with
     * @param channel A {@link Channel} object instance
     */
    public void show(Channel channel)
    {
        if (!hiddenChannels.contains(channel)) return;

        hiddenChannels.remove(channel);

        Language.localeChat(player, "CHANNEL_SHOW", getFormatting(channel));
    }

    /**
     * Hides a specific {@link Channel} from a viewer
     * @param channel A {@link Channel} object instance
     */
    public void hide(Channel channel)
    {
        if (hiddenChannels.contains(channel)) return;

        hiddenChannels.add(channel);

        Language.localeChat(player, "CHANNEL_HIDE", getFormatting(channel));
    }

    /**
     * Utility method that generates placeholders from a {@link Channel}'s {@link ChannelProperties}
     * @param channel The {@link Channel} that formatting is being generated for
     * @return A {@link HashMap} with placeholder replacements that are used when localing the chat
     */
    private HashMap<String, Object> getFormatting(Channel channel) {
        return new Placeholders()
                .put("color", channel.getProperties().getColor())
                .put("name", channel.getDisplayName())
                .build();
    }

}
