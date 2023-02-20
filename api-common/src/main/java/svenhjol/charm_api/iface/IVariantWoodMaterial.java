package svenhjol.charm_api.iface;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public interface IVariantWoodMaterial extends IVariantMaterial {
    default int chestBoatColor() {
        return 0xdf9f43;
    }

    BlockSetType getBlockSetType();

    WoodType getWoodType();
}
