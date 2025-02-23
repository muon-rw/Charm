package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlock;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.GunpowderBlock;
import svenhjol.charm.feature.storage_blocks.sugar_block.SugarBlock;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;

import java.util.List;

@Feature(priority = 1, description = """
    Adds more item blocks.
    Disabling this feature will disable all related storage block features.""")
public final class StorageBlocks extends CommonFeature {
    public StorageBlocks(CommonLoader loader) {
        super(loader);
    }

    @Override
    public List<ChildFeature<StorageBlocks>> children() {
        return List.of(
            new EnderPearlBlock(loader()),
            new GunpowderBlock(loader()),
            new SugarBlock(loader())
        );
    }
}
