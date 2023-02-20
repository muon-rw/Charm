package svenhjol.charm_core.fabric.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.iface.IIgniteProvider;

import java.util.function.Supplier;

public class CharmStairBlock extends StairBlock implements IIgniteProvider {
    protected final CharmFeature feature;
    protected final IVariantMaterial variantMaterial;

    public CharmStairBlock(CharmFeature feature, IVariantMaterial material, BlockState state) {
        super(state, material.blockProperties());
        this.feature = feature;
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
        public <T extends Block> BlockItem(CharmFeature feature, Supplier<T> block) {
            super(feature, block, new Properties());
        }
    }
}
