package svenhjol.charm.mixin.extra_stackables;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    /**
     * Change behavior to drop a bottle if the inventory is full.
     */
    @Redirect(
        method = "finishUsingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"
        )
    )

    private boolean hookGetInventory(Inventory inventory, ItemStack bottle) {
        inventory.player.getInventory().placeItemBackInInventory(bottle);
        return false;
    }

    /**
     * This is just for Dehydration compat
     * Essentially rebuilding their mixin that allows emptying potions into water source blocks,
     * but not emptying the potion if it's a stack size greater than 1.
     *
     * This is pretty cursed, but it's probably fine.
     */
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void useMixin(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> info) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = Item.getPlayerPOVHitResult(world, player, ClipContext.Fluid.WATER);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);

            if (world.mayInteract(player, blockPos) && blockState.getFluidState().is(FluidTags.WATER)) {
                if (stack.getCount() == 1) {
                    world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0f, 1.0f);
                    info.setReturnValue(InteractionResultHolder.sidedSuccess(new ItemStack(Items.GLASS_BOTTLE), world.isClientSide()));
                } else {
                    info.setReturnValue(InteractionResultHolder.pass(stack));
                }
            }
        }
    }
}
