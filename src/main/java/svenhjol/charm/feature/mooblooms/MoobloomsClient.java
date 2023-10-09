package svenhjol.charm.feature.mooblooms;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.function.Supplier;

@ClientFeature(mod = CharmClient.MOD_ID, feature = Mooblooms.class)
public class MoobloomsClient extends CharmonyFeature {
    static Supplier<ModelLayerLocation> layer;

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();

        layer = registry.modelLayer(
            () -> new ModelLayerLocation(Charm.instance().makeId("moobloom"), "main"), CowModel::createBodyLayer);

        registry.entityRenderer(Mooblooms.entity,
            () -> ctx -> new MoobloomEntityRenderer<>(ctx, new CowModel<>(ctx.bakeLayer(layer.get()))));
    }

    @Override
    public void runWhenEnabled() {
        CharmClient.instance().registry()
            .itemTab(Mooblooms.spawnEggItem, CreativeModeTabs.SPAWN_EGGS, Items.AXOLOTL_SPAWN_EGG);
    }
}
