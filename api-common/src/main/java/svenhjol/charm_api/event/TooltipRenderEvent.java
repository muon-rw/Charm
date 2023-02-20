package svenhjol.charm_api.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class TooltipRenderEvent extends CharmEvent<TooltipRenderEvent.Handler> {
    public static final TooltipRenderEvent INSTANCE = new TooltipRenderEvent();

    private TooltipRenderEvent() {}

    public void invoke(PoseStack poseStack, List<ClientTooltipComponent> lines, int x, int y, @Nullable ItemStack stack) {
        for (Handler handler : getHandlers()) {
            handler.run(poseStack, lines, x, y, stack);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(PoseStack poseStack, List<ClientTooltipComponent> lines, int x, int y, @Nullable ItemStack stack);
    }
}
