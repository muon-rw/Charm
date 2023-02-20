package svenhjol.charm_core.base;

import net.minecraft.world.level.block.Block;

public abstract class CharmBlock extends Block {
    protected final CharmFeature feature;

    public CharmBlock(CharmFeature feature, Properties properties) {
        super(properties);
        this.feature = feature;
    }

    public boolean isEnabled() {
        return feature.isEnabled();
    }
}
