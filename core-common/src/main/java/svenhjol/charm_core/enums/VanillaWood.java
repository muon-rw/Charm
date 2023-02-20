package svenhjol.charm_core.enums;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm_api.iface.IVariantWoodMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum VanillaWood implements IVariantWoodMaterial {
    ACACIA(() -> BlockSetType.ACACIA, () -> WoodType.ACACIA, MaterialColor.COLOR_ORANGE, 0xda8357, true),
    BIRCH(() -> BlockSetType.BIRCH, () -> WoodType.BIRCH, MaterialColor.SAND, 0xffefa0, true),
    CHERRY(() -> BlockSetType.CHERRY, () -> WoodType.CHERRY, MaterialColor.TERRACOTTA_WHITE, 0xffaf9f, true),
    CRIMSON(() -> BlockSetType.CRIMSON, () -> WoodType.CRIMSON, MaterialColor.CRIMSON_STEM, 0x9f4a6f, false),
    DARK_OAK(() -> BlockSetType.DARK_OAK, () -> WoodType.DARK_OAK, MaterialColor.COLOR_BROWN, 0x6b4224, true),
    JUNGLE(() -> BlockSetType.JUNGLE, () -> WoodType.JUNGLE, MaterialColor.DIRT, 0xd08f67, true),
    MANGROVE(() -> BlockSetType.MANGROVE, () -> WoodType.MANGROVE, MaterialColor.COLOR_RED, 0xaf5944, true),
    OAK(() -> BlockSetType.OAK, () -> WoodType.OAK, MaterialColor.WOOD, 0xf0ba70, true),
    SPRUCE(() -> BlockSetType.SPRUCE, () -> WoodType.SPRUCE, MaterialColor.PODZOL, 0x91683e, true),
    WARPED(() -> BlockSetType.WARPED, () -> WoodType.WARPED, MaterialColor.WARPED_STEM, 0x408a82, false);

    private final boolean flammable;
    private final MaterialColor materialColor;
    private final int chestBoatColor;
    private final Supplier<BlockSetType> blockSetType;
    private final Supplier<WoodType> woodType;

    VanillaWood(Supplier<BlockSetType> blockSetType, Supplier<WoodType> woodType, MaterialColor materialColor, int chestBoatColor, boolean flammable) {
        this.blockSetType = blockSetType;
        this.woodType = woodType;
        this.materialColor = materialColor;
        this.chestBoatColor = chestBoatColor;
        this.flammable = flammable;
    }

    @Override
    public boolean isFlammable() {
        return flammable;
    }

    @Override
    public Material material() {
        return Material.WOOD;
    }

    @Override
    public MaterialColor materialColor() {
        return materialColor;
    }

    @Override
    public int chestBoatColor() {
        return chestBoatColor;
    }

    @Override
    public SoundType soundType() {
        return SoundType.WOOD;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public BlockSetType getBlockSetType() {
        return blockSetType.get();
    }

    @Override
    public WoodType getWoodType() {
        return woodType.get();
    }

    public static List<IVariantWoodMaterial> getTypes() {
        return Arrays.stream(values())
            .collect(Collectors.toList());
    }

    public static List<IVariantWoodMaterial> getTypesWithout(IVariantWoodMaterial... types) {
        var typesList = new ArrayList<>(Arrays.asList(types));
        return Arrays.stream(values()).filter(t -> !typesList.contains(t))
            .collect(Collectors.toList());
    }
}
