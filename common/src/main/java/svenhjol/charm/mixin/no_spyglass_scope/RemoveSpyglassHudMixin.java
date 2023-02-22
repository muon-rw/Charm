package svenhjol.charm.mixin.no_spyglass_scope;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.no_spyglass_scope.NoSpyglassScope;

@Mixin(Gui.class)
public abstract class RemoveSpyglassHudMixin {
    @Shadow private float scopeScale;

    @Shadow protected abstract void renderSpyglassOverlay(PoseStack poseStack, float scopeScale);

    /**
     * Defer to shouldRemoveHud. If the check is false, render as normal.
     */
    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;renderSpyglassOverlay(Lcom/mojang/blaze3d/vertex/PoseStack;F)V"
        )
    )
    public void hookRender(Gui instance, PoseStack poseStack, float v) {
        if (!NoSpyglassScope.shouldRemoveHud()) {
            this.renderSpyglassOverlay(poseStack, this.scopeScale);
        }
    }
}
