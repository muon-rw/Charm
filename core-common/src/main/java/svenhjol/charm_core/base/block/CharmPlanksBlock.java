package svenhjol.charm_core.base.block;

import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.base.CharmBlock;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.iface.IIgniteProvider;

import java.util.function.Supplier;

public class CharmPlanksBlock extends CharmBlock implements IIgniteProvider {
    protected final IVariantMaterial variantMaterial;

    public CharmPlanksBlock(CharmFeature feature, IVariantMaterial material) {
        super(feature, material.blockProperties()
            .strength(2.0F, 3.0F));

        this.variantMaterial = material;
    }

    @Override
    public int igniteChance() {
        return variantMaterial.isFlammable() ? 5 : 0;
    }

    @Override
    public int burnChance() {
        return variantMaterial.isFlammable() ? 20 : 0;
    }

    public static class BlockItem extends CharmBlockItem {
        public BlockItem(CharmFeature feature, Supplier<CharmPlanksBlock> block) {
            super(feature, block, new Properties());
        }
    }
}
