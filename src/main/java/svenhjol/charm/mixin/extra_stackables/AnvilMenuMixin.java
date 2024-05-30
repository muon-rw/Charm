package svenhjol.charm.mixin.extra_stackables;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

/**
 * Changes the behavior of stacked enchanted books to only apply one book at a time
 * Mostly done to prevent cost-capping exploits with DragonLoot
 * Mixin priority is shifted slightly later due to a conflicting mixin from Charmony.
 */
@Mixin(value = AnvilMenu.class, priority = 1100)
public abstract class AnvilMenuMixin {
    @Shadow
    public int repairItemCountCost;

    private boolean leftStackEnchantedBooks() {
        ItemStack leftSlot = ((AnvilMenu)(Object)this).inputSlots.getItem(0);
        return leftSlot.getItem() == Items.ENCHANTED_BOOK && leftSlot.getCount() > 1;
    }
    private boolean rightStackEnchantedBooks() {
        ItemStack rightSlot = ((AnvilMenu)(Object)this).inputSlots.getItem(1);
        return rightSlot.getItem() == Items.ENCHANTED_BOOK && rightSlot.getCount() > 1;
    }

    /**
     * Always calculate the level cost as if there is only 1 item in the left stack
     *
     * Why is it hard coded to cost 40 levels if the stack size is over 1?
     * The world will never know
     */
    @ModifyExpressionValue(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getCount()I",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/inventory/AnvilMenu;broadcastChanges()V"
                    )
            )
    )
    private int fakeInputStackSize(int original) {
        if (leftStackEnchantedBooks()) {
            return 1;
        }
        return original;
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
     *
     * Causes the right slot shrink by 1 instead of clearing
     * without needing to mixin to multiple setItems or set temporary flags
     *
     * Also prevents XP cost from increasing when multiple books are
     * placed into the right stack.
     *
     */
    @Redirect(
            method = "createResult",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/inventory/AnvilMenu;repairItemCountCost:I",
                    opcode = Opcodes.PUTFIELD
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