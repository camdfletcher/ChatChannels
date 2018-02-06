package com.codenameflip.chatchannels.structure;

import com.codenameflip.chatchannels.utils.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/3/18
 */
@RequiredArgsConstructor
public class Channel {

    /**
     * The identifier that the API will use to reference the channel, specified in the
     */
    @Getter
    private final String identifier;

    /**
     * The formatted, pretty name for the channel; used for all front-end displays
     */
    @Getter
    private final String displayName;

    /**
     * All string aliases that can also be used to reference this channel; primarily used for commands
     */
    @Getter
    private final List<String> aliases;

    /**
     * All properties that influence how the channel functions. See: {@link ChannelProperties}
     */
    @Getter
    private final ChannelProperties properties;

    /**
     * A list of viewers currently focused/chatting in the channel
     */
    @Getter
    private Set<UUID> focusedPlayers = new HashSet<>();

    /**
     * A list of viewers who currently have the channel shown
     */
    @Getter
    private Set<UUID> viewingPlayers = new HashSet<>();

    /**
     * Utility method used to make it easy to locale entire channels
     *
     * @param message The language message identifier you wish to send
     * @param placeholders The placeholders that will be used when localing the chat
     */
    public void broadcast(String message, HashMap<String, Object> placeholders) {
        viewingPlayers.forEach(uuid -> {
            Player target = Bukkit.getPlayer(uuid);

            if (target == null) return; // Skip!

            Language.localeChat(target, message, placeholders);
        });
    }

    /**
     * Utility method to send raw bukkit messages to all players in a channel
     *
     * @param message The message you wish to send
     */
    public void broadcastRaw(String message) {
        viewingPlayers.forEach(uuid -> {
            Player target = Bukkit.getPlayer(uuid);

            if (target == null) return; // Skip!

            target.sendMessage(Language.color(message));
        });
    }

}
