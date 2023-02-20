package svenhjol.charm.feature.mooblooms;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@ClientFeature
public class MoobloomsClient extends CharmFeature {
    static Supplier<ModelLayerLocation> LAYER;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(Mooblooms.class));
    }

    @Override
    public void register() {
        LAYER = CharmClient.REGISTRY.modelLayer(
            () -> new ModelLayerLocation(Charm.makeId("moobloom"), "main"), CowModel::createBodyLayer);

        CharmClient.REGISTRY.entityRenderer(Mooblooms.ENTITY, () ->
            ctx -> new MoobloomEntityRenderer<>(ctx, new CowModel<>(ctx.bakeLayer(LAYER.get()))));

        if (isEnabled()) {
            CharmClient.REGISTRY.itemTab(Mooblooms.SPAWN_EGG_ITEM, CreativeModeTabs.SPAWN_EGGS, Items.AXOLOTL_SPAWN_EGG);
        }
    }
}
