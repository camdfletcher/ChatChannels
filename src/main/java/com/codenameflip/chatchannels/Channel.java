package com.codenameflip.chatchannels;

import com.codenameflip.chatchannels.utilities.Datastore;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 2/3/18
 */
@Builder
public class Channel implements Datastore {

    @Getter String identifier;
    @Getter String displayName;
    @Getter Set<ChannelViewer> focusedPlayers = new HashSet<>();
    @Getter Set<ChannelViewer> viewingPlauers = new HashSet<>();

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }

}
