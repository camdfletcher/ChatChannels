package com.codenameflip.chatchannels.listeners;

import com.codenameflip.chatchannels.ChatChannels;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.stream.Stream;

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

        // Check to see if a new update has been cached
        if (player.hasPermission("chatchannels.update.notify")) {
            System.out.println("[ChatChannels] Checking for plugin update...");
            Object[] update = ChatChannels.get().getUpdateHandler().getLatestUpdate();

            System.out.println("[ChatChannels] Update result = " + Arrays.toString(update));

            if (update != null) {
                String newVersion = (String) update[0];
                String newChanges = (String) update[1];

                System.out.println("[ChatChannels] New version number: " + newVersion);
                System.out.println("[ChatChannels] New version information: " + newChanges);

                final String TAG = ChatColor.RED + "[ChatChannels] ";

                Stream.of(
                        TAG + ChatColor.GOLD + "a " + ChatColor.BOLD + "new update " + ChatColor.GOLD + "has been released.",
                        TAG + ChatColor.DARK_AQUA + "New Version " + ChatColor.WHITE+ newVersion,
                        TAG + ChatColor.DARK_AQUA + "Your Version " + ChatColor.WHITE + ChatChannels.get().getDescription().getVersion(),
                        TAG + ChatColor.DARK_AQUA + "Plugin Update(s) " + ChatColor.WHITE + newChanges
                ).forEach(player::sendMessage);
            }
        }
    }

}
