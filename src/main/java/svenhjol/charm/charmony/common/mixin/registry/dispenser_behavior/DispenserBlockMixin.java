package svenhjol.charm.charmony.common.mixin.registry.dispenser_behavior;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.common.CommonRegistry;
import svenhjol.charm.charmony.common.dispenser.CompositeDispenseItemBehavior;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {
    @ModifyReturnValue(
        method = "getDispenseMethod",
        at = @At("RETURN"),
            remap = true
    )
    private DispenseItemBehavior hookGetDispenseMethod(DispenseItemBehavior original, Level level, ItemStack stack) {
        if (CommonRegistry.conditionalDispenserBehaviors().containsKey(stack.getItem())) {
            return new CompositeDispenseItemBehavior(original);
        }
        return original;
    }
}
