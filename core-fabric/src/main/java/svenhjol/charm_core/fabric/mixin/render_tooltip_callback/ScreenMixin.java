package svenhjol.charm_core.fabric.mixin.render_tooltip_callback;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm_core.fabric.event.RenderTooltipCallback;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin {
    protected ItemStack itemStack;

    /**
     * Fires the {@link RenderTooltipCallback} event.
     * Modules can hook into the tooltip before it is rendered.
     */
    @Inject(
        method = "renderTooltipInternal",
        at = @At("HEAD")
    )
    private void hookRenderOrderedTooltip(PoseStack poseStack, List<ClientTooltipComponent> lines, int x, int y, ClientTooltipPositioner clientTooltipPositioner, CallbackInfo ci) {
        RenderTooltipCallback.EVENT.invoker().interact((Screen)(Object)this, poseStack, itemStack, lines, x, y);
        itemStack = null;
    }

    /**
     * Caches the ItemStack passed to getTooltipFromItem.
     * This is then passed to the RenderTooltipEvent event above.
     */
    @Inject(
        method = "getTooltipFromItem",
        at = @At("HEAD")
    )
    private void hookGetTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<Component>> cir) {
        itemStack = stack;
    }
}
