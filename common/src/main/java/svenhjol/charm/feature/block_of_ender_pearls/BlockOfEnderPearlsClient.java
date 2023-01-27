package svenhjol.charm.feature.block_of_ender_pearls;

import net.minecraft.world.item.CreativeModeTabs;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class BlockOfEnderPearlsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(BlockOfEnderPearls.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            CharmClient.REGISTRY.itemTab(
                BlockOfEnderPearls.BLOCK_ITEM,
                CreativeModeTabs.BUILDING_BLOCKS,
                null
            );
        }
    }
}
