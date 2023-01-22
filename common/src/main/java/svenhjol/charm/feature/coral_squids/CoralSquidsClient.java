package svenhjol.charm.feature.coral_squids;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

@ClientFeature
public class CoralSquidsClient extends CharmFeature {
    private static Supplier<ModelLayerLocation> LAYER;

    @Override
    public void register() {
        LAYER = CharmClient.REGISTRY.modelLayer(
            () -> new ModelLayerLocation(Charm.makeId("coral_squid"), "main"), CoralSquidEntityModel::getTexturedModelData);

        CharmClient.REGISTRY.entityRenderer(CoralSquids.ENTITY, () ->
            ctx -> new CoralSquidEntityRenderer<>(ctx, new CoralSquidEntityModel<>(ctx.bakeLayer(LAYER.get()))));

        var enabled = Charm.LOADER.isEnabled(CoralSquids.class);

        addDependencyCheck(m -> enabled);
        if (enabled) {
            CharmClient.REGISTRY.itemTab(CoralSquids.BUCKET_ITEM, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.AXOLOTL_BUCKET);
            CharmClient.REGISTRY.itemTab(CoralSquids.SPAWN_EGG_ITEM, CreativeModeTabs.SPAWN_EGGS, Items.AXOLOTL_SPAWN_EGG);
        }
    }
}
