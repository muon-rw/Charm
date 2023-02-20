package svenhjol.charm_core.base;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public abstract class CharmBlockItem extends BlockItem {
    protected final CharmFeature feature;

    public <T extends Block> CharmBlockItem(CharmFeature feature, Supplier<T> block, Properties properties) {
        super(block.get(), properties);
        this.feature = feature;
    }

    public boolean isEnabled() {
        return feature.isEnabled();
    }
}
