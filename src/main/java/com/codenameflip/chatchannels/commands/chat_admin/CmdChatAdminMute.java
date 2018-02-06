package com.codenameflip.chatchannels.commands.chat_admin;

import com.codenameflip.chatchannels.ChatChannels;
import com.codenameflip.chatchannels.structure.Channel;
import com.codenameflip.chatchannels.utils.Language;
import com.codenameflip.chatchannels.utils.Placeholders;
import com.simplexitymc.command.api.ChildCommand;
import com.simplexitymc.command.api.Command;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/5/18
 */
public class CmdChatAdminMute extends ChildCommand {

    CmdChatAdminMute(Command parent, String... executors) {
        super(parent, executors);
    }

    @Override
    public void execute(Player player, String... strings) {
        if (strings.length < 1) player.chat("/chatAdmin");
        else {
            Optional<Channel> targetChannel = ChatChannels.getInstance().getRegistry().getChannel(strings[0]);

            if (!targetChannel.isPresent())
                Language.localeChat(player, "INVALID_PARAM", new Placeholders("param", "channel").build());
            else {
                Channel channel = targetChannel.get();

                boolean muted = !channel.getProperties().isMuted(); // Invert the boolean to "toggle" the mute
                channel.getProperties().setMuted(muted);

                if (muted) channel.broadcast("CHAT_MUTED", new Placeholders("executor", player.getName()).build());
                else channel.broadcast("CHAT_UNMUTED", new Placeholders("executor", player.getName()).build());
            }
        }
    }

}
