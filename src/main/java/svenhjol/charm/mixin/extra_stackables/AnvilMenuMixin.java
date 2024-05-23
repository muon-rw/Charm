package svenhjol.charm.mixin.extra_stackables;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {
    /**
     * Changes the behavior of stacked enchanted books to only combine one book at a time
     * Prevents weird exploits with mods like DragonLoot
     */
    @Shadow
    private int repairItemCountCost;

    private boolean leftStackEnchantedBooks() {
        ItemStack leftSlot = ((AnvilMenu)(Object)this).inputSlots.getItem(0);
        return leftSlot.getItem() == Items.ENCHANTED_BOOK && leftSlot.getCount() > 1;
    }

    private boolean rightStackEnchantedBooks() {
        ItemStack rightSlot = ((AnvilMenu)(Object)this).inputSlots.getItem(1);
        return rightSlot.getItem() == Items.ENCHANTED_BOOK && rightSlot.getCount() > 1;
    }

    /**
     * Calculate the level cost as if there is only 1 item in the left stack
     */
    @Redirect(
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
    private int fakeInputStackSize(ItemStack itemStack) {
        if (leftStackEnchantedBooks()) {
            return 1;
        }
        return itemStack.getCount();
    }

    /**
     * Change the output to 1 book at a time
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
     * Shrink the right slot by 1 instead of clearing it
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

    // This doesn't work lol
    @Inject(
            method = "onTake",
            at = @At("RETURN")
    )
    private void recreateResult(Player player, ItemStack stack, CallbackInfo ci) {
        ((AnvilMenu)(Object)this).inputSlots.setChanged();
    }

}