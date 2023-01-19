package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.ArrayList;
import java.util.Collections;

@ClientFeature
public class CoralSeaLanternsClient extends CharmFeature {
    @Override
    public void register() {
        var enabled = Charm.LOADER.isEnabled(CoralSeaLanterns.class);
        addDependencyCheck(m -> enabled);

        if (enabled) {
            var values = new ArrayList<>(CoralSeaLanterns.BLOCK_ITEMS.values());
            Collections.reverse(values);

            for (var value : values) {
                CharmClient.REGISTRY.itemTab(value, CreativeModeTabs.BUILDING_BLOCKS, Items.SEA_LANTERN);
                CharmClient.REGISTRY.itemTab(value, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.SEA_LANTERN);
            }
        }
    }
}
