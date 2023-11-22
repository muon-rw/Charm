package svenhjol.charm.feature.player_pressure_plates;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import svenhjol.charmony.base.CharmonyBlockItem;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class PlayerPressurePlateBlock extends PressurePlateBlock {
    public PlayerPressurePlateBlock() {
        super(BlockSetType.STONE, Properties.of()
            .requiresCorrectToolForDrops()
            .noCollission()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(2F, 1200F));
    }

    @Override
    protected int getSignalStrength(@Nonnull Level level, @Nonnull BlockPos pos) {
        var bounds = TOUCH_AABB.move(pos);
        var entities = level.getEntitiesOfClass(Player.class, bounds);
        return !entities.isEmpty() ? 15 : 0;
    }

    public static class BlockItem extends CharmonyBlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block, new Properties());
        }
    }
}
