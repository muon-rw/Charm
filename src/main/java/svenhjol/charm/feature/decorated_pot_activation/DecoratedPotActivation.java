package svenhjol.charm.feature.decorated_pot_activation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;

import java.util.Optional;

public class DecoratedPotActivation extends CommonFeature {
    static final double PLAYER_RANGE = 4.0d;
    static final float TARGET_UNCERTAINTY = 2.0f; // lower values is more accurate hit with the potiion

    @Configurable(name = "Splash potions", description = "If true, a splash potion will be thrown at a nearby player.")
    public static boolean splashPotions = true;

    @Configurable(name = "Lingering potions", description = "If true, a lingering potion will be activated.")
    public static boolean lingeringPotions = true;

    @Configurable(name = "Spawn eggs", description = "If true, a spawn egg will spawn a mob.")
    public static boolean spawnEggs = true;

    @Configurable(name = "TNT", description = "If true, TNT will be ignited.")
    public static boolean tnt = false;

    @Override
    public String description() {
        return """
            Some items within decorated pots will activate when the pot is broken. Only one item in the stack will be activated, the remainder drop as normal.""";
    }

    public static void activateTopItem(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof DecoratedPotBlockEntity pot && !level.isClientSide) {
            var stack = pot.getTheItem();

            if (tnt && stack.is(Items.TNT)) {
                stack.shrink(1);

                var tnt = new PrimedTnt(level, pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d, null);
                level.addFreshEntity(tnt);
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.gameEvent(null, GameEvent.PRIME_FUSE, pos);

                pot.setFromItem(stack);
            }

            if (lingeringPotions && stack.getItem() instanceof LingeringPotionItem) {
                nearbyPlayer(level, pos).ifPresent(player -> {
                    if (throwPotion(stack, level, pos, player.blockPosition())) {
                        stack.shrink(1);
                        pot.setFromItem(stack);
                    }
                });
            }

            if (splashPotions && stack.getItem() instanceof SplashPotionItem) {
                nearbyPlayer(level, pos).ifPresent(player -> {
                    if (throwPotion(stack, level, pos, player.blockPosition())) {
                        stack.shrink(1);
                        pot.setFromItem(stack);
                    }
                });
            }

            if (spawnEggs && stack.getItem() instanceof SpawnEggItem egg) {
                var entityType = egg.getType(stack.getTag());
                if (entityType.spawn((ServerLevel)level, stack, null, pos, MobSpawnType.SPAWN_EGG, true, true) != null) {
                    stack.shrink(1);
                    pot.setFromItem(stack);
                }
            }
        }
    }

    /**
     * Get a nearby player as a target for lingering/splash potions.
     */
    static Optional<Player> nearbyPlayer(Level level, BlockPos pos) {
        var players = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(PLAYER_RANGE));
        return players.stream().findAny();
    }

    /**
     * Helper method to create a throwable potion from a lingering/splash potion item stack.
     */
    static boolean throwPotion(ItemStack stack, Level level, BlockPos source, BlockPos target) {
        var s = source.getCenter();
        var t = target.getCenter();

        var thrownPotion = new ThrownPotion(level, s.x(), s.y() + 0.75d, s.z());
        thrownPotion.setItem(stack);

        var x = t.x() - s.x();
        var y = t.y() - s.y() + 0.33d;
        var z = t.z() - s.z();
        var i = Math.sqrt(x * x + z * z) * 0.2d;

        thrownPotion.shoot(x, y + i, z, 0.33f, TARGET_UNCERTAINTY);
        return level.addFreshEntity(thrownPotion);
    }
}
