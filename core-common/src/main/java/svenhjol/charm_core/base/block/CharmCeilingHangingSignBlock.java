package svenhjol.charm_core.base.block;

import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.base.CharmFeature;

public class CharmCeilingHangingSignBlock extends CeilingHangingSignBlock {
    protected final CharmFeature feature;
    protected final IVariantWoodMaterial variantMaterial;
    public CharmCeilingHangingSignBlock(CharmFeature feature, IVariantWoodMaterial material, WoodType woodType) {
        super(material.blockProperties().strength(1.0F).noCollission().sound(SoundType.HANGING_SIGN), woodType);

        this.feature = feature;
        this.variantMaterial = material;
    }
}
