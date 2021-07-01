package svenhjol.charm.module.azalea_wood;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.CharmClientModule;

@ClientModule(module = AzaleaWood.class)
public class AzaleaWoodClient extends CharmClientModule {

    @Override
    public void register() {
        // cut-out the transparent areas of the blocks
        BlockRenderLayerMap.INSTANCE.putBlock(AzaleaWood.DOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AzaleaWood.TRAPDOOR, RenderType.cutout());

        // register boat model
        ClientHelper.registerEntityModelLayer(new ResourceLocation(Charm.MOD_ID, "boat/azalea"), BoatModel.createBodyModel().bakeRoot());

        // register sign material
        ClientHelper.registerSignMaterial(AzaleaWood.SIGN_TYPE);
    }
}
