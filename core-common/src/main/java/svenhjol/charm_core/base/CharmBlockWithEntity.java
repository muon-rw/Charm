package svenhjol.charm_core.base;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CharmBlockWithEntity extends BaseEntityBlock {
    protected final CharmFeature feature;

    public CharmBlockWithEntity(CharmFeature feature, Properties properties) {
        super(properties);
        this.feature = feature;
    }

    public boolean isEnabled() {
        return feature.isEnabled();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
