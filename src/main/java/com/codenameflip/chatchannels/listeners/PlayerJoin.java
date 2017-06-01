package com.codenameflip.chatchannels.listeners;

import com.codenameflip.chatchannels.ChatChannels;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.stream.Stream;

/**
 * @author codenameflip
 * @since 4/6/17
 */

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        // If a player has newly logged in, automatically focus to the default channel and force them to view the default channels
        if (ChatChannels.get().getViewingChannels(player).size() == 0)
        {
            ChatChannels.get().getDefaultViewingChannels().forEach(channel ->
                    channel.display(
                            player,
                            !(ChatChannels.get().getConfig().getBoolean("chat-settings.squelch-viewing-message-on-join"))
                    )
            );
        }

        if (ChatChannels.get().getFocusedChannel(player) == null)
        {
            ChatChannels.get().getDefaultChannel().focus(
                    player,
                    !(ChatChannels.get().getConfig().getBoolean("chat-settings.squelch-focus-message-on-join"))
            );
        }

        // Check to see if a new update has been cached
        if (player.hasPermission("chatchannels.update.notify"))
        {

            if (ChatChannels.get().getUpdateHandler().retrieveUpdateInformationIfAvailable().isPresent())
            {
                Object[] updateInformation = ChatChannels.get().getUpdateHandler().retrieveUpdateInformationIfAvailable().get();

                String updatedVersionNumber = (String) updateInformation[0];
                String updatedVersionTitle = (String) updateInformation[1];

                Stream.of(
                        " ",
                        " ******************************************** ",
                        " **    New ChatChannels Plugin Update!     ** ",
                        " ******************************************** ",
                        " **    Version Number: " + updatedVersionNumber,
                        " **    Change(s) in the new version: " + updatedVersionNumber,
                        " ******************************************** ",
                        " "
                ).forEach(System.out::println);

                Stream.of(
                        "" + ChatColor.RED + "[ChatChannels] " + ChatColor.GOLD + "a " + ChatColor.BOLD + "new update " + ChatColor.GOLD + "has been released.",
                        "" + ChatColor.RED + "[ChatChannels] " + ChatColor.DARK_AQUA + "New Version " + ChatColor.WHITE + updatedVersionNumber,
                        "" + ChatColor.RED + "[ChatChannels] " + ChatColor.DARK_AQUA + "Your Version " + ChatColor.WHITE + ChatChannels.get().getDescription().getVersion(),
                        "" + ChatColor.RED + "[ChatChannels] " + ChatColor.DARK_AQUA + "Plugin Update(s) " + ChatColor.WHITE + updatedVersionTitle
                ).forEach(player::sendMessage);
            }
        }
    }

}
