package svenhjol.charm.mixin.decorated_pot_activation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.decorated_pot_activation.DecoratedPotActivation;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockMixin {
    @Inject(
        method = "onRemove",
        at = @At("HEAD")
    )
    private void hookOnRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bl, CallbackInfo ci) {
        // Check the contents and activate if valid
        DecoratedPotActivation.activateTopItem(level, pos);
    }
}
