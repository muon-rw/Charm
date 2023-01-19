package svenhjol.charm.feature.bat_buckets;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature
public class BatBucketsClient extends CharmFeature {
    @Override
    public void register() {
        CharmClient.REGISTRY.itemTab(
            BatBuckets.BAT_BUCKET_ITEM,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.TADPOLE_BUCKET
        );
    }
}
