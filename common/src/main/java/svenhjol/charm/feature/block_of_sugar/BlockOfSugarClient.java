package svenhjol.charm.feature.block_of_sugar;

import net.minecraft.world.item.CreativeModeTabs;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class BlockOfSugarClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(BlockOfSugar.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            CharmClient.REGISTRY.itemTab(
                BlockOfSugar.BLOCK_ITEM,
                CreativeModeTabs.BUILDING_BLOCKS,
                null
            );
        }
    }
}
