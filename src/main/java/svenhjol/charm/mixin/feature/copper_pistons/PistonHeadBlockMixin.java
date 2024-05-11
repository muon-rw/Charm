package svenhjol.charm.mixin.feature.copper_pistons;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.PistonType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.copper_pistons.CopperPistons;
import svenhjol.charm.foundation.Resolve;

@SuppressWarnings("UnreachableCode")
@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin {
    @Shadow
    @Final
    public static EnumProperty<PistonType> TYPE;

    @Inject(
        method = "getCloneItemStack",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState, CallbackInfoReturnable<ItemStack> cir) {
        if (isCopperPistonBlock()) {
            var copperPistons = Resolve.feature(CopperPistons.class);
            copperPistons.log().debug("changing itemstack to copper");
            var newStack = new ItemStack(
                blockState.getValue(TYPE) == PistonType.STICKY
                    ? copperPistons.registers.stickyCopperPistonBlock.get()
                    : copperPistons.registers.copperPistonBlock.get()
            );
            cir.setReturnValue(newStack);
        }
    }

    @Unique
    private boolean isCopperPistonBlock() {
        var pistonBlockState = ((PistonHeadBlock)(Object)this).defaultBlockState();
        return pistonBlockState.is(Resolve.feature(CopperPistons.class).registers.copperPistonBlock.get());
    }
}
