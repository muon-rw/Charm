package svenhjol.charm.feature.storage_blocks.sugar;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.base.CharmonyFallingBlock;
import svenhjol.charmony.api.event.SugarDissolveEvent;

@SuppressWarnings({"deprecation", "BooleanMethodIsAlwaysInverted", "unused"})
public class SugarBlock extends CharmonyFallingBlock {
    static final MapCodec<SugarBlock> CODEC = simpleCodec(SugarBlock::new);

    public SugarBlock() {
        this(Properties.of()
            .sound(SoundType.SAND)
            .strength(0.5F));
    }

    private SugarBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchWater(level, pos, state)) {
            super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!tryTouchWater(level, pos, state)) {
            super.onPlace(state, level, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchWater(Level level, BlockPos pos, BlockState state) {
        var waterBelow = false;

        for (var facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                var below = pos.relative(facing);
                if (level.getBlockState(below).is(Blocks.WATER)) {
                    waterBelow = true;
                    break;
                }
            }
        }

        if (waterBelow) {
            level.globalLevelEvent(2001, pos, Block.getId(level.getBlockState(pos)));

            SugarDissolveEvent.INSTANCE.invoke(level, pos);

            if (!level.isClientSide()) {
                Sugar.triggerDissolvedSugar((ServerLevel) level, pos);
            }
        }

        return waterBelow;
    }

    static class BlockItem extends CharmonyBlockItem {
        public BlockItem() {
            super(Sugar.block, new Properties());
        }
    }
}
