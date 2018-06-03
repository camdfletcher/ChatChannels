package com.codenameflip.chatchannels.structure;

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

    /**
     * For documentation reference the example config channel example
     */

    @Getter
    @Setter
    @Builder.Default
    private String description = "A simple channel.";

    @Getter
    @Setter
    @Builder.Default
    private String permission = "*";

    @Getter
    @Setter
    @Builder.Default
    private String color = "§f";

    @Getter
    @Setter
    @Builder.Default
    private String chatColor = "§7";

    @Getter
    @Setter
    @Builder.Default
    private double cooldown = -1;

    @Getter
    @Setter
    @Builder.Default
    private double chatRadius = 0;

    @Getter
    @Setter
    @Builder.Default
    private boolean showByDefault = true;

    @Getter
    @Setter
    @Builder.Default
    private boolean focusByDefault = true;

    @Getter
    @Setter
    @Builder.Default
    private boolean muted = false;

}
