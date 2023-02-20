package svenhjol.charm.feature.bamboo_wood;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm_api.iface.IVariantWoodMaterial;

import java.util.Locale;

public enum BambooMaterial implements IVariantWoodMaterial {
    BAMBOO;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public boolean isFlammable() {
        return true;
    }

    @Override
    public Material material() {
        return Material.WOOD;
    }

    @Override
    public MaterialColor materialColor() {
        return MaterialColor.TERRACOTTA_YELLOW;
    }

    @Override
    public int chestBoatColor() {
        return 0xffa0a0;
    }

    @Override
    public BlockSetType getBlockSetType() {
        return BlockSetType.BAMBOO;
    }

    @Override
    public WoodType getWoodType() {
        return WoodType.BAMBOO;
    }

    @Override
    public SoundType soundType() {
        return SoundType.WOOD;
    }
}
