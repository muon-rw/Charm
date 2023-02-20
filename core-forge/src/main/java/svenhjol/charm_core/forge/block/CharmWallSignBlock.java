package svenhjol.charm_core.forge.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

public class CharmWallSignBlock extends WallSignBlock {
    public <B extends Block> CharmWallSignBlock(CharmFeature feature, IVariantMaterial material, Supplier<B> drops, WoodType type) {
        super(material.blockProperties()
            .strength(1.0F)
            .noCollission()
            .lootFrom(drops), type);
    }
}
