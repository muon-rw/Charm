package svenhjol.charm.feature.endermite_powder;

import net.minecraft.world.item.CreativeModeTabs;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature
public class EndermitePowderClient extends CharmFeature {
    @Override
    public void register() {
        CharmClient.REGISTRY.entityRenderer(EndermitePowder.ENTITY, () -> EndermitePowderEntityRenderer::new);

        var enabled = Charm.LOADER.isEnabled(EndermitePowder.class);
        addDependencyCheck(m -> enabled);

        if (enabled) {
            CharmClient.REGISTRY.itemTab(EndermitePowder.ITEM, CreativeModeTabs.TOOLS_AND_UTILITIES, null);
        }
    }
}
