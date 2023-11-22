package svenhjol.charm.feature.redstone_sand;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.base.CharmonyFallingBlock;

@SuppressWarnings("deprecation")
public class RedstoneSandBlock extends CharmonyFallingBlock {
    static final MapCodec<RedstoneSandBlock> CODEC = simpleCodec(RedstoneSandBlock::new);

    public RedstoneSandBlock() {
        this(Properties.of()
            .mapColor(MapColor.COLOR_RED)
            .sound(SoundType.SAND)
            .strength(0.5F));
    }

    private RedstoneSandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 15;
    }

    static class BlockItem extends CharmonyBlockItem {
        public BlockItem() {
            super(RedstoneSand.block, new Properties());
        }
    }
}
