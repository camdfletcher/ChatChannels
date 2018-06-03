package com.codenameflip.chatchannels.utils.cooldowns;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownsManager {
    private CooldownsManager() {
    }

    @Getter
    private static List<Cooldowns> trackedCooldowns = new ArrayList<>();

    public static long getTimeRemaining(UUID uuid, String reason) {
        Optional<Cooldowns> cooldown = trackedCooldowns.stream()
                .filter(cooldowns -> cooldowns.getUuid().equals(uuid))
                .filter(cooldowns -> cooldowns.getReason().equalsIgnoreCase(reason))
                .findAny();

        return cooldown.map(cooldowns -> TimeUnit.MILLISECONDS.toSeconds(cooldowns.getExpirationTime() - System.currentTimeMillis())).orElse(-1L);
    }
}
