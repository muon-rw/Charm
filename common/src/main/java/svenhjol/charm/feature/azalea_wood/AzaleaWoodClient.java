package svenhjol.charm.feature.azalea_wood;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class AzaleaWoodClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(AzaleaWood.class));
    }

    @Override
    public void register() {
        // Cut out transparent areas of blocks.
        CharmClient.REGISTRY.blockRenderType(AzaleaWood.DOOR_BLOCK, RenderType::cutout);
        CharmClient.REGISTRY.blockRenderType(AzaleaWood.TRAPDOOR_BLOCK, RenderType::cutout);

        // Register boat models.
        CharmClient.REGISTRY.modelLayer(
            () -> new ModelLayerLocation(Charm.makeId("boat/azalea"), "main"),
            BoatModel::createBodyModel);

        CharmClient.REGISTRY.modelLayer(
            () -> new ModelLayerLocation(Charm.makeId("chest_boat/azalea"), "main"),
            ChestBoatModel::createBodyModel);

        // Register sign material.
        CharmClient.REGISTRY.signMaterial(AzaleaWood.WOOD_TYPE);
    }
}
