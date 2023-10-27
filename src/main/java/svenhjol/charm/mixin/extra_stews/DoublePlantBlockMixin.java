package svenhjol.charm.mixin.extra_stews;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import svenhjol.charm.feature.extra_stews.ExtraStews;

@Mixin(DoublePlantBlock.class)
public class DoublePlantBlockMixin implements SuspiciousEffectHolder {
    @Override
    public MobEffect getSuspiciousEffect() {
        return MobEffects.DAMAGE_BOOST;
    }

    @Override
    public int getEffectDuration() {
        if (isPitcherPlant()) {
            var duration = ExtraStews.getPitcherPlantEffectDuration();
            if (duration > 0) {
                return duration * 20;
            }
        }
        return 0;
    }

    @Unique
    private boolean isPitcherPlant() {
        return (Object)this == Blocks.PITCHER_PLANT;
    }
}
