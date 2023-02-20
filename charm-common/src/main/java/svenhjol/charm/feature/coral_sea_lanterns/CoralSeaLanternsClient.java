package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class CoralSeaLanternsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(CoralSeaLanterns.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            var values = new ArrayList<>(CoralSeaLanterns.BLOCK_ITEMS.values());
            Collections.reverse(values);

            for (var value : values) {
                CharmClient.REGISTRY.itemTab(value, CreativeModeTabs.BUILDING_BLOCKS, Items.SEA_LANTERN);
                CharmClient.REGISTRY.itemTab(value, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.SEA_LANTERN);
            }
        }
    }
}
