package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm_api.iface.IVariantMaterial;

import java.util.Locale;

public enum CoralMaterial implements IVariantMaterial {
    BRAIN(MaterialColor.COLOR_PINK),
    BUBBLE(MaterialColor.COLOR_PURPLE),
    FIRE(MaterialColor.COLOR_RED),
    HORN(MaterialColor.COLOR_YELLOW),
    TUBE(MaterialColor.COLOR_BLUE);

    private final MaterialColor materialColor;

    CoralMaterial(MaterialColor materialColor) {
        this.materialColor = materialColor;
    }

    @Override
    public Material material() {
        return Material.GLASS;
    }

    @Override
    public MaterialColor materialColor() {
        return materialColor;
    }

    @Override
    public SoundType soundType() {
        return SoundType.GLASS;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
