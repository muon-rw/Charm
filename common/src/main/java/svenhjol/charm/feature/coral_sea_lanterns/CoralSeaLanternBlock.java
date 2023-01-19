package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.level.block.Block;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.base.CharmBlock;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

public class CoralSeaLanternBlock extends CharmBlock {
    public CoralSeaLanternBlock(CharmFeature feature, IVariantMaterial material) {
        super(feature, Properties.of(material.material(), material.materialColor())
            .strength(0.3F)
            .sound(material.soundType())
            .lightLevel(blockState -> 15));
    }

    static class BlockItem extends CharmBlockItem {
        public <T extends Block> BlockItem(CharmFeature feature, Supplier<T> block) {
            super(feature, block, new Properties());
        }
    }
}
