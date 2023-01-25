package svenhjol.charm.feature.endermite_powder;

import net.minecraft.world.item.CreativeModeTabs;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class EndermitePowderClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(EndermitePowder.class));
    }

    @Override
    public void register() {
        CharmClient.REGISTRY.entityRenderer(EndermitePowder.ENTITY, () -> EndermitePowderEntityRenderer::new);

        if (isEnabled()) {
            CharmClient.REGISTRY.itemTab(EndermitePowder.ITEM, CreativeModeTabs.TOOLS_AND_UTILITIES, null);
        }
    }
}
