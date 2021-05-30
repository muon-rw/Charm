package svenhjol.charm.block;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.module.CharmModule;

public abstract class CharmPlanksBlock extends CharmBlock {
    public CharmPlanksBlock(CharmModule module, String name, Settings settings) {
        super(module, name, settings);
    }

    public CharmPlanksBlock(CharmModule module, String name, MapColor color) {
        this(module, name, Settings.of(Material.WOOD, color)
            .strength(2.0F, 3.0F)
            .sounds(BlockSoundGroup.WOOD));
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> items) {
        if (enabled())
            super.addStacksForDisplay(group, items);
    }
}