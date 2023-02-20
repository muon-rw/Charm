package svenhjol.charm.feature.variant_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.iface.IFuelProvider;

import java.util.function.Supplier;

public class VariantChestBlock extends ChestBlock implements IVariantChest {
    private final CharmFeature feature;
    private final IVariantMaterial material;

    public VariantChestBlock(IVariantMaterial material) {
        super(Properties.copy(Blocks.CHEST), () -> VariantChests.NORMAL_BLOCK_ENTITY.get());
        this.feature = getParent();
        this.material = material;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VariantChestBlockEntity(pos, state);
    }

    @Override
    public IVariantMaterial getMaterial() {
        return material;
    }

    private static VariantChests getParent() {
        return Charm.LOADER.get(VariantChests.class).orElseThrow();
    }

    static class BlockItem extends CharmBlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantChestBlock> block) {
            super(getParent(), block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
