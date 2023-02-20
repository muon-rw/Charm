package svenhjol.charm.feature.redstone_sand;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class RedstoneSandClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(RedstoneSand.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            CharmClient.REGISTRY.itemTab(
                RedstoneSand.BLOCK,
                CreativeModeTabs.REDSTONE_BLOCKS,
                Items.REDSTONE_BLOCK
            );
        }
    }
}
