package svenhjol.charm.feature.variant_barrels;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class VariantBarrelsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(VariantBarrels.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            for (var item : VariantBarrels.BARREL_BLOCK_ITEMS.values()) {
                CharmClient.REGISTRY.itemTab(
                    item,
                    CreativeModeTabs.FUNCTIONAL_BLOCKS,
                    Items.BARREL
                );
                CharmClient.REGISTRY.itemTab(
                    item,
                    CreativeModeTabs.REDSTONE_BLOCKS,
                    Items.BARREL
                );
            }
        }
    }
}
