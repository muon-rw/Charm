package svenhjol.charm.mixin.make_suspicious_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.make_suspicious_blocks.MakeSuspiciousBlocks;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlockMixin {
    @Inject(
        method = "triggerEvent",
        at = @At("HEAD")
    )
    private void hookTriggerEvent(BlockState state, Level level, BlockPos pos, int signal, int j, CallbackInfoReturnable<Boolean> cir) {
        MakeSuspiciousBlocks.checkAndConvert(level, pos, state);
    }
}
