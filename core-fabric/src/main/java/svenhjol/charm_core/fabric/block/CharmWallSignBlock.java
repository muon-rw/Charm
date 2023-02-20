package svenhjol.charm_core.fabric.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.base.CharmFeature;

public class CharmWallSignBlock extends WallSignBlock {
    public <B extends Block> CharmWallSignBlock(CharmFeature feature, IVariantMaterial material, B drops, WoodType type) {
        super(material.blockProperties()
                .strength(1.0F)
                .noCollission()
                .dropsLike(drops), type);
    }
}
