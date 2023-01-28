package svenhjol.charm.feature.block_of_gunpowder;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class BlockOfGunpowderClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(BlockOfGunpowder.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            CharmClient.REGISTRY.itemTab(
                BlockOfGunpowder.BLOCK_ITEM,
                CreativeModeTabs.REDSTONE_BLOCKS,
                Items.HONEY_BLOCK
            );
        }
    }
}
