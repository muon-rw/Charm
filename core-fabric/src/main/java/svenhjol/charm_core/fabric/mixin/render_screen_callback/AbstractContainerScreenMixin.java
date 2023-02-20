package svenhjol.charm_core.fabric.mixin.render_screen_callback;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm_core.fabric.event.RenderScreenCallback;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    /**
     * Fires the {@link RenderScreenCallback} event.
     * This is called on every vanilla drawForeground tick and allows custom gui rendering.
     */
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderLabels(Lcom/mojang/blaze3d/vertex/PoseStack;II)V"
        )
    )
    private void hookRender(PoseStack poseStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        RenderScreenCallback.EVENT.invoker().interact((AbstractContainerScreen<?>)(Object)this, poseStack, mouseX, mouseY);
    }
}
