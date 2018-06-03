package com.codenameflip.chatchannels.listeners;

import com.codenameflip.chatchannels.utils.Language;
import com.codenameflip.chatchannels.utils.Placeholders;
import com.codenameflip.chatchannels.utils.updater.TrackedUpdate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 02/07/2018
 */
public class JoinListener implements ChatChannelsListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!getRegistry().getFocusedChannel(player.getUniqueId()).isPresent()) { // Only handle if not set already.
            getRegistry().getAutoShowChannels().forEach(all -> getRegistry().showChannel(player, all));
            getRegistry().getAutoFocusChannels().forEach(all -> getRegistry().focusChannel(player, all));
        }

        if (player.hasPermission("chatchannels.notify-update") && get().getUpdateWatcher().getLatestUpdate().isPresent()) {
            TrackedUpdate update = get().getUpdateWatcher().getLatestUpdate().get();

            Language.localeChat(player, "UPDATE_NOTIFICATION",
                    new Placeholders()
                            .put("version", update.getVersion())
                            .put("update", update.getUpdate())
                            .build()
            );
        }
    }

}
