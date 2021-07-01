package svenhjol.charm.module.variant_bars;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmClientModule;

@ClientModule(module = VariantBars.class)
public class VariantBarsClient extends CharmClientModule {
    @Override
    public void register() {
        VariantBars.BARS.values().forEach(bars -> BlockRenderLayerMap.INSTANCE.putBlock(bars, RenderType.cutout()));
    }
}
