package svenhjol.charm.mixin.feature.item_stacking;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {
    @Shadow
    private int repairItemCountCost;

    @Unique
    private boolean leftStackEnchantedBooks() {
        ItemStack leftSlot = ((AnvilMenu) (Object) this).getSlot(AnvilMenu.INPUT_SLOT).getItem();
        return leftSlot.is(Items.ENCHANTED_BOOK) && leftSlot.getCount() > 1;
    }

    @Unique
    private boolean rightStackEnchantedBooks() {
        ItemStack rightSlot = ((AnvilMenu) (Object) this).getSlot(AnvilMenu.ADDITIONAL_SLOT).getItem();
        return rightSlot.is(Items.ENCHANTED_BOOK) && rightSlot.getCount() > 1;
    }


    /**
     * Always calculate the level cost as if there is only 1 item in the left stack
     * <p>
     * Why is it hard coded to cost 40 levels if the stack size is over 1?
     * The world will never know
     */
    @ModifyExpressionValue(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getCount()I",
                    ordinal = 1
            )
    )
    private int fakeInputStackSize(int original) {
        return leftStackEnchantedBooks() ? 1 : original;
    }

    /**
     * Change the output to always be 1 book at a time
     */
    @Redirect(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"
            )
    )
    private void capResultSlot(ResultContainer resultSlots, int slot, ItemStack stack) {
        if (leftStackEnchantedBooks()) {
            stack.setCount(1);
        }
        resultSlots.setItem(slot, stack);
    }


    /**
     * Make createResult think we only have 1 book in the right slot -
     * <p>
     * Causes the right slot shrink by 1 instead of clearing
     * without needing to mixin to multiple setItems or set temporary flags
     * <p>
     * Also prevents XP cost from increasing when multiple books are
     * placed into the right stack.
     */
    @Redirect(
            method = "createResult",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/inventory/AnvilMenu;repairItemCountCost:I"
            )
    )
    private void capRepairItemCountCost(AnvilMenu instance, int value) {
        if (rightStackEnchantedBooks()) {
            repairItemCountCost = 1;
        } else {
            repairItemCountCost = value;
        }
    }

    /**
     * Shrink the left slot by 1 instead of clearing it
     */
    @Redirect(
            method = "onTake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 0
            )
    )
    private void shrinkLeftSlot(Container inv, int index, ItemStack stack) {
        if (leftStackEnchantedBooks()) {
            ItemStack leftSlot = inv.getItem(index);
            leftSlot.shrink(1);
            inv.setItem(index, leftSlot.isEmpty() ? ItemStack.EMPTY : leftSlot);
        } else {
            inv.setItem(index, stack);
        }
    }
}