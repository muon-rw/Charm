package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature(mod = Charm.MOD_ID)
public class SmoothGlowstoneClient extends CharmFeature {
    @Override
    public void register() {
        CharmClient.REGISTRY.itemTab(SmoothGlowstone.BLOCK_ITEM, CreativeModeTabs.BUILDING_BLOCKS, Items.GLOWSTONE);
    }
}
