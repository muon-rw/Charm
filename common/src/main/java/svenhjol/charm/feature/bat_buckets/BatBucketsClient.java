package svenhjol.charm.feature.bat_buckets;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature
public class BatBucketsClient extends CharmFeature {
    @Override
    public void register() {
        var enabled = Charm.LOADER.isEnabled(BatBuckets.class);
        addDependencyCheck(m -> enabled);

        if (enabled) {
            CharmClient.REGISTRY.itemTab(
                BatBuckets.BAT_BUCKET_ITEM,
                CreativeModeTabs.TOOLS_AND_UTILITIES,
                Items.TADPOLE_BUCKET
            );
        }
    }
}
