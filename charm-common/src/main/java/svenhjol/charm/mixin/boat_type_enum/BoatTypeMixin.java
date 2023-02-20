package svenhjol.charm.mixin.boat_type_enum;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.init.BoatTypeEnums;

import java.util.*;

/**
 * Enum solution from LudoCrypt:
 * @link <a href="https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556">...</a>
 * Each charm mod that adds enums should have a BoatTypeEnums with list of strings to inject.
 */
@SuppressWarnings({"target", "unused", "SameParameterValue"})
@Mixin(Boat.Type.class)
public class BoatTypeMixin {
    @Shadow
    @Final
    @Mutable
    private static Boat.Type[] $VALUES;

    static {
        for (var boatTypeEnum : BoatTypeEnums.BOAT_TYPE_ENUMS) {
            addVariant(boatTypeEnum.toUpperCase(Locale.ROOT), Blocks.OAK_PLANKS, boatTypeEnum.toLowerCase(Locale.ROOT));
        }
    }

    @Invoker("<init>")
    public static Boat.Type invokeInit(String internalName, int internalId, Block block, String typeName) {
        throw new AssertionError();
    }

    private static void addVariant(String newName, Block block, String typeName) {
        List<Boat.Type> variants = new ArrayList<>(Arrays.asList(BoatTypeMixin.$VALUES));
        variants.add(invokeInit(newName.toUpperCase(Locale.ROOT), variants.get(variants.size() - 1).ordinal() + 1, block, typeName));
        BoatTypeMixin.$VALUES = variants.toArray(new Boat.Type[0]);

        // Reset the codec index.
        Boat.Type.CODEC = StringRepresentable.fromEnum(Boat.Type::values);
        Boat.Type.BY_ID = ByIdMap.continuous(Enum::ordinal, $VALUES, ByIdMap.OutOfBoundsStrategy.ZERO);
    }
}
