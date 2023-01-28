package svenhjol.charm.feature.ebony_wood;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_barrels.VariantBarrels;
import svenhjol.charm.feature.variant_chests.VariantChests;
import svenhjol.charm.feature.variant_ladders.VariantLadders;
import svenhjol.charm.feature.wood.Wood;
import svenhjol.charm_api.iface.IProvidesWandererTrades;
import svenhjol.charm_api.iface.IRemovesRecipes;
import svenhjol.charm_api.iface.IWandererTrade;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.base.block.*;
import svenhjol.charm_core.init.CharmApi;
import svenhjol.charm_core.init.GlobalLoaders;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "A dark grey wood. " +
    "Ebony trees can be found in savanna biomes.")
public class EbonyWood extends CharmFeature implements IProvidesWandererTrades, IRemovesRecipes {
    static Supplier<WoodType> WOOD_TYPE;
    static Supplier<CharmDoorBlock> DOOR_BLOCK;
    static Supplier<CharmTrapdoorBlock> TRAPDOOR_BLOCK;
    static Supplier<CharmLeavesBlock> LEAVES_BLOCK;
    static Supplier<CharmLogBlock> LOG_BLOCK;
    static Supplier<CharmSaplingBlock> SAPLING_BLOCK;
    static Supplier<Holder<ConfiguredFeature<TreeConfiguration, ?>>> TREE;
    static ResourceKey<ConfiguredFeature<?, ?>> TREE_FEATURE;
    static ResourceKey<ConfiguredFeature<?, ?>> TREES_FEATURE;
    static ResourceKey<PlacedFeature> TREE_PLACEMENT;
    static ResourceKey<PlacedFeature> TREES_PLACEMENT;
    public static TagKey<Biome> GROWS_EBONY_TREES = TagKey.create(Registries.BIOME, Charm.makeId("grows_ebony_trees"));

    @Override
    public void register() {
        var material = EbonyMaterial.EBONY;

        WOOD_TYPE = Wood.registerWoodType(material);
        DOOR_BLOCK = Wood.registerDoor(this, material).getFirst();
        TRAPDOOR_BLOCK = Wood.registerTrapdoor(this, material).getFirst();
        LEAVES_BLOCK = Wood.registerLeaves(this, material).getFirst();

        var log = Wood.registerLog(this, material);
        LOG_BLOCK = log.get("ebony_log").getFirst(); // Need reference to add to tree feature.

        Wood.registerBoat(this, material);
        Wood.registerButton(this, material);
        Wood.registerFence(this, material);
        Wood.registerGate(this, material);
        Wood.registerPlanksSlabsAndStairs(this, material);
        Wood.registerPressurePlate(this, material);
        Wood.registerSign(this, material);

        Wood.registerBarrel(material);
        Wood.registerBookshelf(material);
        Wood.registerChest(material);
        Wood.registerTrappedChest(material);
        Wood.registerLadder(material);

        SAPLING_BLOCK = Wood.registerSapling(this, material).getFirst();

        TREE_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Charm.makeId("ebony_tree"));
        TREES_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Charm.makeId("ebony_trees"));

        TREE_PLACEMENT = ResourceKey.create(Registries.PLACED_FEATURE, Charm.makeId("ebony_tree"));
        TREES_PLACEMENT = ResourceKey.create(Registries.PLACED_FEATURE, Charm.makeId("ebony_trees"));

        CharmApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        Charm.REGISTRY.biomeAddition("ebony_trees", holder -> holder.is(GROWS_EBONY_TREES),
            GenerationStep.Decoration.VEGETAL_DECORATION, TREES_PLACEMENT);
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of();
    }

    @Override
    public List<IWandererTrade> getRareWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return SAPLING_BLOCK.get();
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public int getCost() {
                return 20;
            }
        });
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        List<ResourceLocation> remove = new ArrayList<>();

        if (!Charm.LOADER.isEnabled(VariantBarrels.class)) {
            remove.add(Charm.makeId("ebony_barrel"));
        }

        if (!Charm.LOADER.isEnabled(VariantChests.class)) {
            remove.add(Charm.makeId("ebony_chest"));
            remove.add(Charm.makeId("ebony_trapped_chest"));
        }

        if (!Charm.LOADER.isEnabled(VariantLadders.class)) {
            remove.add(Charm.makeId("ebony_ladder"));
        }

        if (!GlobalLoaders.isEnabled(new ResourceLocation("charm_world", "woodcutters"))) {
            remove.add(Charm.makeId("ebony_wood/woodcutting/"));
        }

        return remove;
    }
}
