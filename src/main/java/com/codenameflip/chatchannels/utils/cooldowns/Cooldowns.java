package com.codenameflip.chatchannels.utils.cooldowns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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

    /**
     * Begins constructing a Cooldown builder for the specified UUID
     * @param uuid The {@link UUID} for the player you'd like to cooldown
     * @return A {@link Cooldowns} object for you to build a Cooldown with
     */
    public static Cooldowns on(UUID uuid) {
        return new Cooldowns(uuid);
    }

    public static Optional<Cooldowns> get(Player player, String cooldownReason) {
        return get(player.getUniqueId(), cooldownReason);
    }

    /**
     * Gets an instance of a {@link Cooldowns} object by referencing a UUID and action id
     *
     * @param uuid The {@link UUID} you'd like to lookup
     * @param cooldownReason The reason id you're searching for
     * @return {@link Optional<Cooldowns>}
     */
    public static Optional<Cooldowns> get(UUID uuid, String cooldownReason) {
        return CooldownsManager.getTrackedCooldowns().stream()
                .filter(cooldown -> cooldown.getUuid().equals(uuid))
                .filter(cooldown -> cooldown.getReason().equalsIgnoreCase(cooldownReason))
                .findAny();
    }

    public static boolean isDone(Player player, String cooldownReason) {
        return isDone(player.getUniqueId(), cooldownReason);
    }

    /**
     * Checks to see if a player is still cooling down from a specified action
     *
     * @param uuid The UUID you'd like to check for
     * @param cooldownReason The action you'd like to check if the cooldown is completed for
     * @return true/false whether the cooldown is done
     */
    public static boolean isDone(UUID uuid, String cooldownReason) {
        boolean done = true;
        Optional<Cooldowns> optionalCooldown = get(uuid, cooldownReason);

        if (optionalCooldown.isPresent() && optionalCooldown.get().getExpirationTime() >= System.currentTimeMillis())
            done = false;

        if (done) {
            new ArrayList<>(CooldownsManager.getTrackedCooldowns()).stream() // Avoid Concurrent Modification Exceptions
                    .filter(cooldown -> cooldown.getUuid().equals(uuid))
                    .filter(cooldowns -> cooldowns.getReason().equalsIgnoreCase(cooldownReason))
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
        this.expirationTime = System.currentTimeMillis() + duration;
        return this;
    }

    public void done() {
        CooldownsManager.getTrackedCooldowns().add(this);
    }

}

