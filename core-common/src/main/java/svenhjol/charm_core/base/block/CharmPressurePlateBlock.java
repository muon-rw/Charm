package svenhjol.charm_core.base.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

public class CharmPressurePlateBlock extends PressurePlateBlock {
    protected final CharmFeature feature;
    protected final IVariantMaterial variantMaterial;

    public CharmPressurePlateBlock(CharmFeature feature, IVariantWoodMaterial material) {
        super(Sensitivity.EVERYTHING, material.blockProperties()
            .strength(0.5F)
            .noCollission(),
            material.getBlockSetType());

        this.feature = feature;
        this.variantMaterial = material;
    }

    public static class BlockItem extends CharmBlockItem {
        public <T extends Block> BlockItem(CharmFeature feature, Supplier<T> block) {
            super(feature, block, new Properties());
        }
    }
}
