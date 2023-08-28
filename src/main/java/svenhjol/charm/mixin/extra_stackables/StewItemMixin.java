package svenhjol.charm.mixin.extra_stackables;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BowlFoodItem.class, SuspiciousStewItem.class})
public class StewItemMixin {
    /**
     * Return an empty bowl to the player if the stack is larger than zero.
     */
    @Inject(
        method = "finishUsingItem",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookFinishUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (cir.getReturnValue().getItem() == Items.BOWL && itemStack.getCount() > 0) {
            if (livingEntity instanceof Player player) {
                player.getInventory().placeItemBackInInventory(new ItemStack(Items.BOWL));
            }
            cir.setReturnValue(itemStack);
        }
    }
}
