package svenhjol.charm_core.fabric.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public interface RenderScreenCallback {
    Event<RenderScreenCallback> EVENT = EventFactory.createArrayBacked(RenderScreenCallback.class, listeners -> (container, poseStack, mouseX, mouseY) -> {
        for (RenderScreenCallback listener : listeners) {
            listener.interact(container, poseStack, mouseX, mouseY);
        }
    });

    void interact(AbstractContainerScreen<?> container, PoseStack poseStack, int mouseX, int mouseY);
}
