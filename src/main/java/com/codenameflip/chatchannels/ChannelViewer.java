package com.codenameflip.chatchannels;

import com.codenameflip.chatchannels.channel.IChannel;
import lombok.Getter;
import org.bukkit.entity.Player;

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

    public ChannelViewer(Player player)
    {
        this.player = player;
    }

    @Getter
    public IChannel focusedChannel;

    @Getter
    public Set<IChannel> hiddenChannels;

    // TODO:

    public void focus(IChannel channel)
    {
    }

    public void show(IChannel channel)
    {
    }

    public void hide(IChannel channel)
    {
    }


}
