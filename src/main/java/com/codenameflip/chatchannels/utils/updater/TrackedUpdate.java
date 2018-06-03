package com.codenameflip.chatchannels.utils.updater;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TrackedUpdate {
    @Getter
    final String version;

    @Getter
    final String update;
}
