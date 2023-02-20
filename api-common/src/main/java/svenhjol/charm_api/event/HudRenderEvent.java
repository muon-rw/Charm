package svenhjol.charm_api.event;

import com.mojang.blaze3d.vertex.PoseStack;

public class HudRenderEvent extends CharmEvent<HudRenderEvent.Handler> {
    public static final HudRenderEvent INSTANCE = new HudRenderEvent();

    private HudRenderEvent() {}

    @FunctionalInterface
    public interface Handler {
        void run(PoseStack poseStack, float tickDelta);
    }
}
