package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import svenhjol.charm.charmony.iface.CustomMaterial;
import svenhjol.charm.charmony.iface.IgniteProvider;

import java.util.function.Supplier;

public class CustomLeavesBlock extends LeavesBlock implements IgniteProvider {
    protected final CustomMaterial material;

    public CustomLeavesBlock(CustomMaterial material) {
        super(Properties.of()
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn((state, world, pos, type) -> false)
            .isSuffocating((state, world, pos) -> false)
            .isViewBlocking((state, world, pos) -> false));

        this.material = material;
    }

    @Override
    public int igniteChance() {
        return material.isFlammable() ? 30 : 0;
    }

    @Override
    public int burnChance() {
        return material.isFlammable() ? 60 : 0;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
