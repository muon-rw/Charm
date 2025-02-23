package svenhjol.charm.feature.arcane_purpur.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.arcane_purpur.ArcanePurpur;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Handlers extends FeatureHolder<ArcanePurpur> {
    public Handlers(ArcanePurpur feature) {
        super(feature);
    }

    public boolean tryChorusTeleport(LivingEntity entity, ItemStack stack) {
        if (entity.level().isClientSide) {
            return false;
        }

        var range = feature().teleportRange();
        if (range == 0) {
            return false;
        }

        var pos = entity.blockPosition();
        var level = (ServerLevel)entity.level();
        Map<Double, BlockPos> candidates = new HashMap<>();

        // Find potential teleport locations around the player.
        BlockPos.betweenClosedStream(
            pos.offset(-range, -range, -range),
            pos.offset(range, range, range)
        ).forEach(p -> {
            var above = p.above(1);
            var state = level.getBlockState(p);

            // Must be a valid chorus teleport block.
            if (!state.is(Tags.CHORUS_TELEPORTS)) {
                return;
            }

            // Can't teleport the player to the same position.
            if (above.equals(pos)) {
                return;
            }

            // Avoid suffocation.
            var state1 = level.getBlockState(p.above(1));
            var state2 = level.getBlockState(p.above(2));
            if (!state1.isAir() || !state2.isAir()) {
                return;
            }

            var dist = getDistanceSquared(pos, above);
            candidates.put(dist, above);
        });

        if (candidates.isEmpty()) return false;

        // Get a single target location from the possible candidates.
        var target = candidates.get(Collections.min(candidates.keySet()));
        if (target == null) return false;

        double x = target.getX() + 0.5d;
        double y = target.getY();
        double z = target.getZ() + 0.5d;

        var didTeleport = entity.randomTeleport(x, y, z, true); // True to show particle effect.
        if (!didTeleport) return false;

        var sound = SoundEvents.CHORUS_FRUIT_TELEPORT;
        level.playSound(null, x, y, z, sound, SoundSource.PLAYERS, 1.0f, 1.0f); // At old location
        entity.playSound(sound, 1.0f, 1.0f); // At new location

        if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCooldowns().addCooldown(Items.CHORUS_FRUIT, 20);
            if (!serverPlayer.getAbilities().instabuild) {
                stack.shrink(1);
            }
            feature().advancements.teleportedToBlock(serverPlayer);
        }

        return true;
    }

    public double getDistanceSquared(BlockPos pos1, BlockPos pos2) {
        double d0 = pos1.getX();
        double d1 = pos1.getZ();
        double d2 = d0 - pos2.getX();
        double d3 = d1 - pos2.getZ();
        return d2 * d2 + d3 * d3;
    }
}