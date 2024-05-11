package svenhjol.charm.mixin.feature.auto_restock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.feature.auto_restock.AutoRestock;
import svenhjol.charm.foundation.feature.FeatureResolver;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin implements FeatureResolver<AutoRestock> {
    /**
     * Allows auto restock of a composted item.
     */
    @Inject(
        method = "useItemOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;levelEvent(ILnet/minecraft/core/BlockPos;I)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void hookOnUse(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        feature().handlers.addItemUsedStat(player, itemStack);
    }

    @Override
    public Class<AutoRestock> typeForFeature() {
        return AutoRestock.class;
    }
}
