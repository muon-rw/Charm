package svenhjol.charm.feature.bat_buckets;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class BatBucketsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(BatBuckets.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            CharmClient.instance().registry().itemTab(
                BatBuckets.bucketItem,
                CreativeModeTabs.TOOLS_AND_UTILITIES,
                Items.TADPOLE_BUCKET
            );
        }
    }
}