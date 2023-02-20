package svenhjol.charm.feature.mooblooms;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

@SuppressWarnings("deprecation")
public class MoobloomFlowerFeatureRenderer<T extends MoobloomEntity> extends RenderLayer<T, CowModel<T>> {
    public MoobloomFlowerFeatureRenderer(RenderLayerParent<T, CowModel<T>> context) {
        super(context);
    }

    /**
     * Copypasta from MushroomCowMushroomLayer with adjustments to scale and another flower added.
     */
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isBaby() && !entity.isInvisible()) {
            var blockRenderManager = Minecraft.getInstance().getBlockRenderer();
            var state = entity.getMoobloomType().getFlower();
            var m = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);

            poseStack.pushPose();
            poseStack.translate(0.2D, -0.35D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-48.0F));
            poseStack.scale(-0.75F, -0.75F, 0.75F);
            poseStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderSingleBlock(state, poseStack, bufferSource, light, m);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.2D, -0.35D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(42.0F));
            poseStack.translate(0.4D, 0.0D, -0.6D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-48.0F));
            poseStack.scale(-0.75F, -0.75F, 0.75F);
            poseStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderSingleBlock(state, poseStack, bufferSource, light, m);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.2D, -0.35D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(42.0F));
            poseStack.translate(-0.05, 0.0D, -0.4D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-48.0F));
            poseStack.scale(-0.75F, -0.75F, 0.75F);
            poseStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderSingleBlock(state, poseStack, bufferSource, light, m);
            poseStack.popPose();

            if (entity.isPollinated()) {
                poseStack.pushPose();
                (this.getParentModel()).getHead().translateAndRotate(poseStack);
                poseStack.translate(0.0D, -0.699999988079071D, -0.20000000298023224D);
                poseStack.mulPose(Axis.YP.rotationDegrees(-78.0F));
                poseStack.scale(-0.75F, -0.75F, 0.75F);
                poseStack.translate(-0.5D, -0.65D, -0.5D);
                blockRenderManager.renderSingleBlock(state, poseStack, bufferSource, light, m);
                poseStack.popPose();
            }
        }
    }
}
