package svenhjol.charm.feature.player_pressure_plates;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm_core.base.CharmBlockItem;
import svenhjol.charm_core.base.CharmFeature;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public class PlayerPressurePlateBlock extends PressurePlateBlock {
    private final CharmFeature feature;

    public PlayerPressurePlateBlock(CharmFeature feature) {
        super(Sensitivity.MOBS, Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
            .requiresCorrectToolForDrops()
            .noCollission()
            .strength(2F, 1200F),
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON);

        this.feature = feature;
    }

    @Override
    protected int getSignalStrength(@Nonnull Level level, @Nonnull BlockPos pos) {
        net.minecraft.world.phys.AABB bounds = TOUCH_AABB.move(pos);
        List<? extends Entity> entities = level.getEntitiesOfClass(Player.class, bounds);
        return entities.size() > 0 ? 15 : 0;
    }

    public static class BlockItem extends CharmBlockItem {
        public <T extends Block> BlockItem(CharmFeature feature, Supplier<T> block) {
            super(feature, block, new Properties());
        }
    }
}
