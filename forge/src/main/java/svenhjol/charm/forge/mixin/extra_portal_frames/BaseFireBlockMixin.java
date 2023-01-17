package svenhjol.charm.forge.mixin.extra_portal_frames;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.extra_portal_frames.ExtraPortalFrames;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {
    @Redirect(
        method = "isPortal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;isPortalFrame(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z",
            remap = false
        )
    )
    private static boolean hookIsPortal(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
        return ExtraPortalFrames.isValidBlockState(state);
    }
}
