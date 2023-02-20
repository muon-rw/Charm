package svenhjol.charm_api.iface;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public interface IVariantMaterial extends StringRepresentable {
    default boolean isFlammable() {
        return true;
    }

    default int fuelTime() {
        return isFlammable() ? 300 : 0;
    }

    default int igniteChance() {
        return isFlammable() ? 30 : 0;
    }

    default int burnChance() {
        return isFlammable() ? 20 : 0;
    }

    Material material();

    MaterialColor materialColor();

    SoundType soundType();

    default BlockBehaviour.Properties blockProperties() {
        return BlockBehaviour.Properties.of(material(), materialColor())
            .sound(soundType());
    }
}
