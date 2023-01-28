package svenhjol.charm.feature.wood;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_barrels.VariantBarrels;
import svenhjol.charm.feature.variant_bookshelves.VariantBookshelves;
import svenhjol.charm.feature.variant_chest_boats.VariantChestBoats;
import svenhjol.charm.feature.variant_chests.VariantChests;
import svenhjol.charm.feature.variant_ladders.VariantLadders;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.base.block.*;
import svenhjol.charm_core.base.item.CharmBoatItem;
import svenhjol.charm_core.base.item.CharmSignItem;
import svenhjol.charm_core.mixin.accessor.BlockItemAccessor;
import svenhjol.charm_core.mixin.accessor.StandingAndWallBlockItemAccessor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "deprecation", "UnusedReturnValue"})
@Feature(mod = Charm.MOD_ID, switchable = false, description = "Registers custom wood types.")
public class Wood extends CharmFeature {
    private static final Map<Boat.Type, ResourceLocation> BOAT_PLANKS_IDENTIFIERS = new HashMap<>();
    private static final Map<Boat.Type, Supplier<CharmBoatItem>> TYPE_TO_BOAT = new HashMap<>();
    private static final Map<Boat.Type, Supplier<CharmBoatItem>> TYPE_TO_CHEST_BOAT = new HashMap<>();
    private static final List<Supplier<CharmSignItem>> SIGN_ITEMS = new ArrayList<>();

    @Override
    public void runWhenEnabled() {
        // Boat planks can't be set early so resolve them here.
        for (var entry : BOAT_PLANKS_IDENTIFIERS.entrySet()) {
            var planks = BuiltInRegistries.BLOCK.getOptional(entry.getValue());
            planks.ifPresent(block -> entry.getKey().planks = block);
        }

        // Sign blocks can't be set early so resolve them here.
        for (var supplier : SIGN_ITEMS) {
            var sign = supplier.get();
            ((StandingAndWallBlockItemAccessor)sign).setWallBlock(sign.getWallSignBlock().get());
            ((BlockItemAccessor)sign).setBlock(sign.getSignBlock().get());
        }
    }

    public static Pair<Supplier<CharmBoatItem>, Supplier<CharmBoatItem>> registerBoat(CharmFeature feature, IVariantMaterial material) {
        var woodName = material.getSerializedName();
        var boatType = Boat.Type.valueOf((Charm.MOD_ID + "_" + woodName).toUpperCase(Locale.ROOT));
        var boat = Charm.REGISTRY.item(woodName + "_boat", () -> new CharmBoatItem(feature, false, boatType));
        var chestBoat = Charm.REGISTRY.item(woodName + "_chest_boat", () -> new CharmBoatItem(feature, true, boatType));

        TYPE_TO_BOAT.put(boatType, boat);
        TYPE_TO_CHEST_BOAT.put(boatType, chestBoat);
        BOAT_PLANKS_IDENTIFIERS.put(boatType, Charm.makeId(material.getSerializedName() + "_planks"));

        VariantChestBoats.registerChestBoat(boat, chestBoat);
        VariantChestBoats.registerChestLayerColor(material);

        return Pair.of(boat, chestBoat);
    }

    public static Pair<Supplier<CharmWoodButtonBlock>, Supplier<CharmWoodButtonBlock.BlockItem>> registerButton(CharmFeature feature, IVariantMaterial material) {
        var id = material.getSerializedName() + "_button";
        var button = Charm.REGISTRY.block(id, () -> new CharmWoodButtonBlock(feature, material));
        var buttonItem = Charm.REGISTRY.item(id, () -> new CharmWoodButtonBlock.BlockItem(feature, button));
        return Pair.of(button, buttonItem);
    }

    public static Pair<Supplier<CharmDoorBlock>, Supplier<CharmDoorBlock.BlockItem>> registerDoor(CharmFeature feature, IVariantMaterial material) {
        var id = material.getSerializedName() + "_door";
        var door = Charm.REGISTRY.block(id, () -> new CharmDoorBlock(feature, material));
        var doorItem = Charm.REGISTRY.item(id, () -> new CharmDoorBlock.BlockItem(feature, door));
        return Pair.of(door, doorItem);
    }

    public static Pair<Supplier<CharmFenceBlock>, Supplier<CharmFenceBlock.BlockItem>> registerFence(CharmFeature feature, IVariantMaterial material) {
        var id = material.getSerializedName() + "_fence";
        var fence = Charm.REGISTRY.block(id, () -> new CharmFenceBlock(feature, material));
        var fenceItem = Charm.REGISTRY.item(id, () -> new CharmFenceBlock.BlockItem(feature, fence));
        Charm.REGISTRY.ignite(fence); // Fences can set on fire.
        return Pair.of(fence, fenceItem);
    }

    public static Pair<Supplier<CharmFenceGateBlock>, Supplier<CharmFenceGateBlock.BlockItem>> registerGate(CharmFeature feature, IVariantMaterial material) {
        var id = material.getSerializedName() + "_fence_gate";
        var gate = Charm.REGISTRY.block(id, () -> new CharmFenceGateBlock(feature, material));
        var gateItem = Charm.REGISTRY.item(id, () -> new CharmFenceGateBlock.BlockItem(feature, gate));
        Charm.REGISTRY.ignite(gate); // Gates can set on fire.
        return Pair.of(gate, gateItem);
    }

    public static Pair<Supplier<CharmLeavesBlock>, Supplier<CharmLeavesBlock.BlockItem>> registerLeaves(CharmFeature feature, IVariantMaterial material) {
        var id = material.getSerializedName() + "_leaves";
        var leaves = Charm.REGISTRY.block(id, () -> new CharmLeavesBlock(feature, material));
        var leavesItem = Charm.REGISTRY.item(id, () -> new CharmLeavesBlock.BlockItem(feature, leaves));
        Charm.REGISTRY.ignite(leaves); // Leaves can set on fire.
        return Pair.of(leaves, leavesItem);
    }

    public static Map<String, Pair<Supplier<CharmLogBlock>, Supplier<CharmLogBlock.BlockItem>>> registerLog(CharmFeature feature, IVariantMaterial material) {
        var logId = material.getSerializedName() + "_log";
        var woodId = material.getSerializedName() + "_wood";
        var strippedLogId = "stripped_" + material.getSerializedName() + "_log";
        var strippedWoodId = "stripped_" + material.getSerializedName() + "_wood";

        var log = Charm.REGISTRY.block(logId, () -> new CharmLogBlock(feature, material));
        var logItem = Charm.REGISTRY.item(logId, () -> new CharmLogBlock.BlockItem(feature, log));
        var strippedLog = Charm.REGISTRY.block(strippedLogId, () -> new CharmLogBlock(feature, material));
        var strippedLogItem = Charm.REGISTRY.item(strippedLogId, () -> new CharmLogBlock.BlockItem(feature, strippedLog));

        var wood = Charm.REGISTRY.block(woodId, () -> new CharmLogBlock(feature, material));
        var woodItem = Charm.REGISTRY.item(woodId, () -> new CharmLogBlock.BlockItem(feature, wood));
        var strippedWood = Charm.REGISTRY.block(strippedWoodId, () -> new CharmLogBlock(feature, material));
        var strippedWoodItem = Charm.REGISTRY.item(strippedWoodId, () -> new CharmLogBlock.BlockItem(feature, strippedWood));

        // Logs and wood can set on fire.
        Charm.REGISTRY.ignite(log);
        Charm.REGISTRY.ignite(wood);
        Charm.REGISTRY.ignite(strippedLog);
        Charm.REGISTRY.ignite(strippedWood);

        // Logs and wood can be stripped.
        Charm.REGISTRY.strippable(log, strippedLog);
        Charm.REGISTRY.strippable(wood, strippedWood);

        Map<String, Pair<Supplier<CharmLogBlock>, Supplier<CharmLogBlock.BlockItem>>> map = new HashMap<>();

        map.put(logId, Pair.of(log, logItem));
        map.put(woodId, Pair.of(wood, woodItem));
        map.put(strippedLogId, Pair.of(strippedLog, strippedLogItem));
        map.put(strippedWoodId, Pair.of(strippedWood, strippedWoodItem));

        return map;
    }

    public static Pair<Supplier<CharmPressurePlateBlock>, Supplier<CharmPressurePlateBlock.BlockItem>> registerPressurePlate(CharmFeature feature, IVariantMaterial material) {
        var id = material.getSerializedName() + "_pressure_plate";
        var pressurePlate = Charm.REGISTRY.block(id, () -> new CharmPressurePlateBlock(feature, material));
        var pressurePlateItem = Charm.REGISTRY.item(id, () -> new CharmPressurePlateBlock.BlockItem(feature, pressurePlate));
        return Pair.of(pressurePlate, pressurePlateItem);
    }

    public static Map<String, Pair<Supplier<? extends Block>, Supplier<? extends BlockItem>>> registerPlanksSlabsAndStairs(CharmFeature feature, IVariantMaterial material) {
        var planksId = material.getSerializedName() + "_planks";
        var slabId = material.getSerializedName() + "_slab";
        var stairsId = material.getSerializedName() + "_stairs";

        var planks = Charm.REGISTRY.block(planksId, () -> new CharmPlanksBlock(feature, material));
        var planksItem = Charm.REGISTRY.item(planksId, () -> new CharmPlanksBlock.BlockItem(feature, planks));

        var slab = Charm.REGISTRY.block(slabId, () -> new CharmSlabBlock(feature, material));
        var slabItem = Charm.REGISTRY.item(slabId, () -> new CharmSlabBlock.BlockItem(feature, slab));

        var stairs = Charm.REGISTRY.stairsBlock(stairsId, feature, material, () -> planks.get().defaultBlockState());

        Charm.REGISTRY.ignite(planks); // Planks can set on fire.
        Charm.REGISTRY.ignite(slab); // Slabs can set on fire.
        Charm.REGISTRY.ignite(stairs.getFirst()); // Stairs can set on fire.

        Map<String, Pair<Supplier<? extends Block >, Supplier<? extends BlockItem>>> map = new HashMap<>();

        map.put(planksId, Pair.of(planks, planksItem));
        map.put(slabId, Pair.of(slab, slabItem));
        map.put(stairsId, Pair.of(stairs.getFirst(), stairs.getSecond()));

        return map;
    }

    public static Pair<Supplier<CharmSaplingBlock>, Supplier<CharmSaplingBlock.BlockItem>> registerSapling(CharmFeature feature, IVariantMaterial material) {
        var saplingId = material.getSerializedName() + "_sapling";
        var treeId = material.getSerializedName() + "_tree";
        var key = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(feature.getModId(), treeId));

        var sapling = Charm.REGISTRY.block(saplingId, () -> new CharmSaplingBlock(feature, material, new AbstractTreeGrower() {
            @Override
            protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasBees) {
                return key;
            }
        }));
        var saplingItem = Charm.REGISTRY.item(saplingId, () -> new CharmSaplingBlock.BlockItem(feature, sapling));

        return Pair.of(sapling, saplingItem);
    }

    public static Map<String, Pair<Supplier<? extends SignBlock>, Supplier<? extends BlockItem>>> registerSign(CharmFeature feature, IVariantMaterial material) {
        var signId = material.getSerializedName() + "_sign";
        var wallSignId = material.getSerializedName() + "_wall_sign";
        var woodType = getWoodType(feature, material);

        var sign = Charm.REGISTRY.block(signId, () -> new CharmStandingSignBlock(feature, material, woodType));
        var wallSign = Charm.REGISTRY.wallSignBlock(wallSignId, feature, material, sign, woodType);
        var signItem = Charm.REGISTRY.item(signId, () -> new CharmSignItem(feature, material, sign, wallSign));

        SIGN_ITEMS.add(signItem);

        Map<String, Pair<Supplier<? extends SignBlock>, Supplier<? extends BlockItem>>> map = new HashMap<>();
        map.put(signId, Pair.of(sign, signItem));
        map.put(wallSignId, Pair.of(wallSign, signItem));

        // Associate with the sign block entity.
        Charm.REGISTRY.blockEntityBlocks(() -> BlockEntityType.SIGN, List.of(sign, wallSign));

        return map;
    }

    public static Pair<Supplier<CharmTrapdoorBlock>, Supplier<CharmTrapdoorBlock.BlockItem>> registerTrapdoor(CharmFeature feature, IVariantMaterial material) {
        var id = material.getSerializedName() + "_trapdoor";
        var trapdoor = Charm.REGISTRY.block(id, () -> new CharmTrapdoorBlock(feature, material));
        var trapdoorItem = Charm.REGISTRY.item(id, () -> new CharmTrapdoorBlock.BlockItem(feature, trapdoor));
        return Pair.of(trapdoor, trapdoorItem);
    }

    public static void registerBarrel(IVariantMaterial material) {
        VariantBarrels.registerBarrel(material);
    }

    public static void registerBookshelf(IVariantMaterial material) {
        VariantBookshelves.registerBookshelf(material);
    }

    public static void registerChest(IVariantMaterial material) {
        VariantChests.registerChest(material);
    }

    public static void registerTrappedChest(IVariantMaterial material) {
        VariantChests.registerTrappedChest(material);
    }

    public static void registerLadder(IVariantMaterial material) {
        VariantLadders.registerLadder(material);
    }

    public static Supplier<WoodType> registerWoodType(IVariantMaterial material) {
        return Charm.REGISTRY.woodType(material.getSerializedName());
    }

    @Nullable
    public static BoatItem getBoatByType(Boat.Type boatType) {
        return TYPE_TO_BOAT.getOrDefault(boatType, () -> null).get();
    }

    @Nullable
    public static BoatItem getChestBoatByType(Boat.Type boatType) {
        return TYPE_TO_CHEST_BOAT.getOrDefault(boatType, () -> null).get();
    }

    public static WoodType getWoodType(CharmFeature feature, IVariantMaterial material) {
        return getWoodTypeByName(Charm.MOD_ID + "_" + material.getSerializedName());
    }

    public static WoodType getWoodTypeByName(String name) {
        return WoodType.values().filter(w -> w.name().equals(name)).findFirst().orElseThrow();
    }
}
