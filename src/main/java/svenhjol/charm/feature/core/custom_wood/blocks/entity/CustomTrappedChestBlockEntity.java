package svenhjol.charm.feature.core.custom_wood.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.charmony.Resolve;

public class CustomTrappedChestBlockEntity extends CustomChestBlockEntity {
    public CustomTrappedChestBlockEntity(BlockPos pos, BlockState state) {
        this(Resolve.feature(CustomWood.class).registers.trappedChestBlockEntity.get(), pos, state);
    }

    public CustomTrappedChestBlockEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    @Override
    protected void signalOpenCount(Level level, BlockPos blockPos, BlockState blockState, int i, int j) {
        super.signalOpenCount(level, blockPos, blockState, i, j);
        if (i != j) {
            Block block = blockState.getBlock();
            level.updateNeighborsAt(blockPos, block);
            level.updateNeighborsAt(blockPos.below(), block);
        }
    }
}
