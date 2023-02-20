package svenhjol.charm.feature.block_of_gunpowder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.Charm;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;

@SuppressWarnings({"deprecation", "BooleanMethodIsAlwaysInverted", "unused"})
public class GunpowderBlock extends FallingBlock {
    public GunpowderBlock() {
        super(Properties.of(Material.SAND)
            .sound(SoundType.SAND)
            .strength(0.5F));
    }

    public static CharmFeature getParent() {
        return Charm.LOADER.get(BlockOfGunpowder.class).orElseThrow();
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchLava(level, pos, state)) {
            super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!BlockOfGunpowder.dissolve || !tryTouchLava(level, pos, state)) {
            super.onPlace(state, level, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchLava(Level level, BlockPos pos, BlockState state) {
        var lavaBelow = false;

        for (var facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                var below = pos.relative(facing);
                if (level.getBlockState(below).getMaterial() == Material.LAVA) {
                    lavaBelow = true;
                    break;
                }
            }
        }

        if (lavaBelow) {
            level.globalLevelEvent(2001, pos, Block.getId(level.getBlockState(pos)));
            level.removeBlock(pos, true);
            level.playSound(null, pos, BlockOfGunpowder.DISSOLVE_SOUND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        if (!level.isClientSide()) {
            // TODO: advancement.
        }

        return lavaBelow;
    }

    static class BlockItem extends CharmBlockItem {
        public BlockItem() {
            super(getParent(), BlockOfGunpowder.BLOCK, new Properties());
        }
    }
}
