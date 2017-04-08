package com.codenameflip.chatchannels.listeners;

import com.codenameflip.chatchannels.ChatChannels;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author codenameflip
 * @since 4/6/17
 */

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // If a player has newly logged in, automatically focus to the default channel and force them to view the default channels
        if (ChatChannels.get().getViewingChannels(player).size() == 0)
            ChatChannels.get().getDefaultViewingChannels().forEach(channel -> channel.display(player));

        if (ChatChannels.get().getFocusedChannel(player) == null)
            ChatChannels.get().getDefaultChannel().focus(player);
    }

}
