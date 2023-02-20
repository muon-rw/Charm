package svenhjol.charm_core.base.block;

import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.base.CharmFeature;

public class CharmStandingSignBlock extends StandingSignBlock {
    protected final CharmFeature feature;
    protected final IVariantWoodMaterial variantMaterial;

    public CharmStandingSignBlock(CharmFeature feature, IVariantWoodMaterial material, WoodType type) {
        super(material.blockProperties().strength(1.0F).noCollission(), type);

        this.feature = feature;
        this.variantMaterial = material;
    }
}
