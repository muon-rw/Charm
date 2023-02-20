package svenhjol.charm_core.base.block;

import net.minecraft.world.level.block.DoorBlock;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

public class CharmDoorBlock extends DoorBlock {
    protected final CharmFeature feature;

    public CharmDoorBlock(CharmFeature feature, IVariantWoodMaterial material) {
        super(material.blockProperties()
            .strength(3.0F),
            material.getBlockSetType());
        this.feature = feature;
    }

    public static class BlockItem extends CharmBlockItem {
        public BlockItem(CharmFeature feature, Supplier<CharmDoorBlock> block) {
            super(feature, block, new Properties());
        }
    }
}
