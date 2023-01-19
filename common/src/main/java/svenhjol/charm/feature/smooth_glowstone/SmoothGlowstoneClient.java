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
        var enabled = Charm.LOADER.isEnabled(SmoothGlowstone.class);
        addDependencyCheck(m -> enabled);

        if (enabled) {
            CharmClient.REGISTRY.itemTab(SmoothGlowstone.BLOCK_ITEM, CreativeModeTabs.BUILDING_BLOCKS, Items.AMETHYST_BLOCK);
        }
    }
}
