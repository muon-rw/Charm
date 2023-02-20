package svenhjol.charm_core.base.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.iface.IIgniteProvider;

import java.util.function.Supplier;

public class CharmLeavesBlock extends LeavesBlock implements IIgniteProvider {
    protected final CharmFeature feature;
    protected final IVariantMaterial variantMaterial;

    public CharmLeavesBlock(CharmFeature feature, IVariantMaterial material) {
        super(Properties.of(Material.LEAVES)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn((state, world, pos, type) -> false)
            .isSuffocating((state, world, pos) -> false)
            .isViewBlocking((state, world, pos) -> false));

        this.feature = feature;
        this.variantMaterial = material;
    }

    @Override
    public int igniteChance() {
        return variantMaterial.isFlammable() ? 30 : 0;
    }

    @Override
    public int burnChance() {
        return variantMaterial.isFlammable() ? 60 : 0;
    }

    public static class BlockItem extends CharmBlockItem {
        public <T extends Block> BlockItem(CharmFeature feature, Supplier<T> block) {
            super(feature, block, new Properties());
        }
    }
}
