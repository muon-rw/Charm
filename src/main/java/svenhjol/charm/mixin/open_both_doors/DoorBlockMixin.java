package svenhjol.charm.mixin.open_both_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.open_both_doors.OpenBothDoors;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {
    @Inject(
        method = "use",
        at = @At("RETURN")
    )
    private void hookUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue() == InteractionResult.CONSUME || cir.getReturnValue() == InteractionResult.SUCCESS) {
            OpenBothDoors.tryOpenNeighbour(level, state, pos, state.getValue(DoorBlock.OPEN));
        }
    }

    @Inject(
        method = "setOpen",
        at = @At("RETURN")
    )
    private void hookSetOpen(Entity entity, Level level, BlockState state, BlockPos pos, boolean bl, CallbackInfo ci) {
        if (entity != null) {
            OpenBothDoors.tryOpenNeighbour(level, state, pos, bl);
        }
    }

    @Inject(
        method = "neighborChanged",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
        )
    )
    private void hookNeighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean bl, CallbackInfo ci) {
        OpenBothDoors.tryOpenNeighbour(level, state, pos, !state.getValue(DoorBlock.OPEN));
    }

    @Inject(
        method = "playSound",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookPlaySound(Entity entity, Level level, BlockPos pos, boolean isClosed, CallbackInfo ci) {
        if (OpenBothDoors.NEIGHBOURS.contains(pos)) {
            OpenBothDoors.NEIGHBOURS.remove(pos);
            ci.cancel();
        }
    }
}
