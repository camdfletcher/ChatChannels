package com.codenameflip.chatchannels.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 02/07/2018
 */
@RequiredArgsConstructor
public class Cooldowns {

    @Getter
    private final UUID uuid;

    @Getter
    private String reason = "";

    @Getter
    private long expirationTime = System.currentTimeMillis();

    /**
     * Static chain-builder methods
     */

    public static Cooldowns on(Player player) {
        return on(player.getUniqueId());
    }

    public static Cooldowns on(UUID uuid) {
        return new Cooldowns(uuid);
    }

    public static Optional<Cooldowns> get(Player player, String cooldownReason) {
        return get(player.getUniqueId(), cooldownReason);
    }

    public static Optional<Cooldowns> get(UUID uuid, String cooldownReason) {
        return CooldownsManager.getTrackedCooldowns().stream()
                .filter(cooldown -> cooldown.getUuid().equals(uuid))
                .filter(cooldown -> cooldown.getReason().equalsIgnoreCase(cooldownReason))
                .findAny();
    }

    public static boolean isDone(Player player, String cooldownReason) {
        return isDone(player.getUniqueId(), cooldownReason);
    }

    public static boolean isDone(UUID uuid, String cooldownReason) {
        boolean done = get(uuid, cooldownReason)
                .map(cooldowns -> cooldowns.getExpirationTime() <= System.currentTimeMillis())
                .orElse(true); // Assume completion

        if (done) {
            new ArrayList<>(CooldownsManager.getTrackedCooldowns()).stream() // Avoid Concurrent Modification Exceptions
                    .filter(cooldown -> cooldown.getUuid().equals(uuid))
                    .forEach(CooldownsManager.getTrackedCooldowns()::remove);
        }

        return done;
    }

    /**
     * Initialization methods
     */

    public Cooldowns forReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Cooldowns forTime(long duration) {
        this.expirationTime = duration;
        return this;
    }

    public void done() {
        CooldownsManager.getTrackedCooldowns().add(this);
    }

}

final class CooldownsManager {
    private CooldownsManager() {
    }

    @Getter
    private static List<Cooldowns> trackedCooldowns = new ArrayList<>();
}
