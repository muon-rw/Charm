package svenhjol.charm.mixin.extra_stews;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.TallFlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import svenhjol.charm.feature.extra_stews.ExtraStews;

@Mixin(TallFlowerBlock.class)
public class TallFlowerBlockMixin implements SuspiciousEffectHolder {
    @Override
    public MobEffect getSuspiciousEffect() {
        return MobEffects.HEALTH_BOOST;
    }

    @Override
    public int getEffectDuration() {
        if (isSunflower()) {
            var duration = ExtraStews.getSunflowerEffectDuration();
            if (duration > 0) {
                return duration * 20;
            }
        }
        return 0;
    }

    @Unique
    private boolean isSunflower() {
        return (Object)this == Blocks.SUNFLOWER;
    }
}
