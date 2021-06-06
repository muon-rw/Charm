package svenhjol.charm.module.variant_mob_textures;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.SnowGolemRenderer;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.*;
import svenhjol.charm.mixin.accessor.EntityRenderDispatcherAccessor;
import svenhjol.charm.mixin.accessor.RenderLayerAccessor;
import svenhjol.charm.mixin.accessor.LivingEntityRendererAccessor;

import java.util.List;

public class VariantMobRenderer {
    private static <T extends LivingEntity, M extends EntityModel<T>> void fillLayersFromOld(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> renderer, EntityType<T> type) {
        EntityRenderer<?> old = ((EntityRenderDispatcherAccessor)context.getEntityRenderDispatcher()).getRenderers().get(type);
        if (old != null) {
            List<RenderLayer<T, M>> layerRenderers = ((LivingEntityRendererAccessor<T, M>) renderer).getLayers();
            layerRenderers.clear();
            ((LivingEntityRendererAccessor<T, M>) old).getLayers()
                .stream()
                .peek(it -> ((RenderLayerAccessor<T, M>)it).setRenderer(renderer))
                .forEach(layerRenderers::add);
        }
    }

    public static class RenderChicken extends ChickenRenderer {
        public RenderChicken(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.CHICKEN);
        }

        @Override
        public ResourceLocation getTextureLocation(Chicken entity) {
            return VariantMobTexturesClient.getChickenTexture(entity);
        }
    }

    public static class RenderCow extends CowRenderer {
        public RenderCow(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.COW);
        }

        @Override
        public ResourceLocation getTextureLocation(Cow entity) {
            return VariantMobTexturesClient.getCowTexture(entity);
        }
    }

    public static class RenderPig extends PigRenderer {
        public RenderPig(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.PIG);
        }

        @Override
        public ResourceLocation getTextureLocation(Pig entity) {
            return VariantMobTexturesClient.getPigTexture(entity);
        }
    }

    public static class RenderSheep extends SheepRenderer {
        public RenderSheep(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.SHEEP);
        }

        @Override
        public ResourceLocation getTextureLocation(Sheep entity) {
            return VariantMobTexturesClient.getSheepTexture(entity);
        }
    }

    public static class RenderSnowGolem extends SnowGolemRenderer {
        public RenderSnowGolem(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.SNOW_GOLEM);
        }

        @Override
        public ResourceLocation getTextureLocation(SnowGolem entity) {
            return VariantMobTexturesClient.getSnowGolemTexture(entity);
        }
    }

    public static class RenderSquid extends SquidRenderer {
        public RenderSquid(EntityRendererProvider.Context context) {
            super(context, new SquidModel(context.bakeLayer(ModelLayers.SQUID)));
            fillLayersFromOld(context, this, EntityType.SQUID);
        }

        @Override
        public ResourceLocation getTextureLocation(Squid entity) {
            return VariantMobTexturesClient.getSquidTexture(entity);
        }
    }

    public static class RenderWolf extends WolfRenderer {
        public RenderWolf(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.WOLF);
        }

        @Override
        public ResourceLocation getTextureLocation(Wolf entity) {
            return VariantMobTexturesClient.getWolfTexture(entity);
        }
    }
}
