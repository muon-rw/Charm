package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charm_core.base.CharmBlock;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

public class SmoothGlowstoneBlock extends CharmBlock {
    public SmoothGlowstoneBlock(CharmFeature feature) {
        super(feature, Properties.copy(Blocks.GLOWSTONE));
    }

    public static class BlockItem extends CharmBlockItem {
        public BlockItem(CharmFeature feature, Supplier<SmoothGlowstoneBlock> block) {
            super(feature, block, new Properties());
        }
    }
}
