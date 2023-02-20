package svenhjol.charm_core.base.block;

import net.minecraft.world.level.block.ButtonBlock;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

public class CharmWoodButtonBlock extends ButtonBlock {
    protected final CharmFeature feature;

    public CharmWoodButtonBlock(CharmFeature feature, IVariantWoodMaterial material) {
        super(material.blockProperties()
            .strength(0.5F),
            material.getBlockSetType(),
            30,
            true);
        this.feature = feature;
    }

    public static class BlockItem extends CharmBlockItem {
        public BlockItem(CharmFeature feature, Supplier<CharmWoodButtonBlock> block) {
            super(feature, block, new Properties());
        }
    }
}
