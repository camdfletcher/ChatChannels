package com.codenameflip.chatchannels.channel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/4/18
 */
@Builder
public class ChannelProperties {

    @Getter
    @Setter
    private String description = "A simple channel.";

    @Getter
    @Setter
    private String permission = "*";

    @Getter
    @Setter
    private String color = "§f";

    @Getter
    @Setter
    private String chatColor = "§7";

    @Getter
    @Setter
    private double cooldown = 0;

    @Getter
    @Setter
    private double chatRadius = 0;

    @Getter
    @Setter
    private boolean showByDefault = true;

    @Getter
    @Setter
    private boolean focusByDefault = true;

}
