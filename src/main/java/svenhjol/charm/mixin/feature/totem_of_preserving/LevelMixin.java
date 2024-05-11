package svenhjol.charm.mixin.feature.totem_of_preserving;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.foundation.Resolve;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Shadow
    public abstract ResourceKey<Level> dimension();

    @Inject(
        method = "destroyBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookDestroyBlock(BlockPos pos, boolean bl, Entity entity, int i, CallbackInfoReturnable<Boolean> cir) {
        if (isProtected(pos)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
        method = "removeBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookRemoveBlock(BlockPos pos, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (isProtected(pos)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
        method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookSetBlock(BlockPos pos, BlockState blockState, int i, int j, CallbackInfoReturnable<Boolean> cir) {
        if (isProtected(pos)) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean isProtected(BlockPos pos) {
        var dimension = this.dimension().location();
        var feature = Resolve.feature(TotemOfPreserving.class);
        return feature.handlers.protectedPositions.containsKey(dimension)
            && feature.handlers.protectedPositions.get(dimension).contains(pos);
    }
}
