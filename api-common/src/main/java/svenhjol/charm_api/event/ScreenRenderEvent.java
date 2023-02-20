package svenhjol.charm_api.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class ScreenRenderEvent extends CharmEvent<ScreenRenderEvent.Handler> {
    public static final ScreenRenderEvent INSTANCE = new ScreenRenderEvent();

    private ScreenRenderEvent() {}

    @FunctionalInterface
    public interface Handler {
        /**
         * Called on every container screen refresh.
         * @param container Reference to the open container.
         * @param poseStack Current rendering stack.
         * @param mouseX Mouse X position.
         * @param mouseY Mouse Y position.
         */
        void run(AbstractContainerScreen<?> container, PoseStack poseStack, int mouseX, int mouseY);
    }
}
