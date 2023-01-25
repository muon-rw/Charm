package svenhjol.charm.feature.player_pressure_plates;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class PlayerPressurePlatesClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(PlayerPressurePlates.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
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
