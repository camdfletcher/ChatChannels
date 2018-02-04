package com.codenameflip.chatchannels;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/3/18
 */
public class ChannelViewer {

    @Getter private final Player player;

    public ChannelViewer(Player player)
    {
        this.player = player;
    }

    @Getter public Channel focusedChannel;
    @Getter public Set<Channel> hiddenChannels;

    public void focus(Channel channel) {

    }

    public void show(Channel channel) {

    }

    public void hide(Channel channel) {

    }


}
