package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import svenhjol.charm.loader.CharmCommonModule;

public class CharmChainBlock extends ChainBlock implements ICharmBlock {
    private final CharmCommonModule module;

    public CharmChainBlock(CharmCommonModule module, String name, Properties settings) {
        super(settings);
        this.module = module;
        this.register(module, name);
    }

    public CharmChainBlock(CharmCommonModule module, String name) {
        this(module, name, Properties.copy(Blocks.CHAIN));
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemCategory(group, items);
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}

