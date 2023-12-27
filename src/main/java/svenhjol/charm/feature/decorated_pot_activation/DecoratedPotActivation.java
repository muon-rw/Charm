package svenhjol.charm.feature.decorated_pot_activation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import svenhjol.charmony.common.CommonFeature;

public class DecoratedPotActivation extends CommonFeature {
    @Override
    public String description() {
        return """
            Some items within decorated pots will activate when the pot is broken.""";
    }

    public static void activateTopItem(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof DecoratedPotBlockEntity pot && !level.isClientSide) {
            var stack = pot.getTheItem();

            if (stack.is(Items.TNT)) {
                stack.shrink(1);

                var tnt = new PrimedTnt(level, pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d, null);
                level.addFreshEntity(tnt);
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.gameEvent(null, GameEvent.PRIME_FUSE, pos);

                pot.setFromItem(stack);
                return;
            }

            if (stack.getItem() instanceof SplashPotionItem) {
                var players = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(4.0));
                var opt = players.stream().findAny();

                if (opt.isPresent()) {
                    var thrownPotion = new ThrownPotion(level, pos.getX(), pos.getY() + 0.75d, pos.getZ());
                    var player = opt.get();
                    thrownPotion.setItem(stack);
                    var x = player.getX() - pos.getX();
                    var y = player.getY(0.33f) - pos.getY();
                    var z = player.getZ() - pos.getZ();
                    var s = Math.sqrt(x * x + z * z) * 0.2d;
                    thrownPotion.shoot(x, y + s, z, 0.33f, 2f);
                    level.addFreshEntity(thrownPotion);

                    stack.shrink(1);
                    pot.setFromItem(stack);
                }

                return;
            }

            if (stack.getItem() instanceof SpawnEggItem egg) {
                var entityType = egg.getType(stack.getTag());
                if (entityType.spawn((ServerLevel)level, stack, null, pos, MobSpawnType.SPAWN_EGG, true, true) != null) {
                    stack.shrink(1);
                    pot.setFromItem(stack);
                }
            }
        }
    }
}
