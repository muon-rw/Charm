package svenhjol.charm.feature.player_pressure_plates;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature
public class PlayerPressurePlatesClient extends CharmFeature {
    @Override
    public void register() {
        var enabled = Charm.LOADER.isEnabled(PlayerPressurePlates.class);
        addDependencyCheck(m -> enabled);

        if (enabled) {
            // Add to the Building Blocks menu tab.
            CharmClient.REGISTRY.itemTab(
                PlayerPressurePlates.BLOCK_ITEM,
                CreativeModeTabs.BUILDING_BLOCKS,
                Items.GILDED_BLACKSTONE
            );
            // Add to the Redstone Blocks menu tab.
            CharmClient.REGISTRY.itemTab(
                PlayerPressurePlates.BLOCK_ITEM,
                CreativeModeTabs.REDSTONE_BLOCKS,
                Items.HEAVY_WEIGHTED_PRESSURE_PLATE
            );
        }
    }
}
