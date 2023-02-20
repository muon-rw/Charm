package svenhjol.charm_core.fabric.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.base.CharmFeature;

public class CharmWallHangingSignBlock extends WallHangingSignBlock {
    public <B extends Block> CharmWallHangingSignBlock(CharmFeature feature, IVariantWoodMaterial material, B drops, WoodType type) {
        super(material.blockProperties()
                .strength(1.0F)
                .noCollission()
                .dropsLike(drops), type);
    }
}
