package svenhjol.charm.mixin.longer_suspicious_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.FlowerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.longer_suspicious_effects.LongerSuspiciousEffects;

@Mixin(FlowerBlock.class)
public abstract class FlowerBlockMixin {

    @Shadow public abstract MobEffect getSuspiciousEffect();

    @Inject(
        method = "getEffectDuration",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookGetEffectDuration(CallbackInfoReturnable<Integer> cir) {
        var currentDuration = cir.getReturnValue();
        var effect = getSuspiciousEffect();

        if (!effect.isInstantenous() && effect.isBeneficial()) {
            cir.setReturnValue(currentDuration * LongerSuspiciousEffects.effectMultiplier);
        }
    }
}
