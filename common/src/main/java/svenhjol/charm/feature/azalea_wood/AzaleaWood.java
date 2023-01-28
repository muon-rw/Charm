package svenhjol.charm.feature.azalea_wood;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_barrels.VariantBarrels;
import svenhjol.charm.feature.variant_chests.VariantChests;
import svenhjol.charm.feature.variant_ladders.VariantLadders;
import svenhjol.charm.feature.wood.Wood;
import svenhjol.charm_api.event.LevelLoadEvent;
import svenhjol.charm_api.iface.IRemovesRecipes;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.base.block.CharmDoorBlock;
import svenhjol.charm_core.base.block.CharmLogBlock;
import svenhjol.charm_core.base.block.CharmTrapdoorBlock;
import svenhjol.charm_core.init.CharmApi;
import svenhjol.charm_core.init.GlobalLoaders;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.")
public class AzaleaWood extends CharmFeature implements IRemovesRecipes {
    static Supplier<WoodType> WOOD_TYPE;
    static Supplier<CharmDoorBlock> DOOR_BLOCK;
    static Supplier<CharmTrapdoorBlock> TRAPDOOR_BLOCK;
    static Supplier<CharmLogBlock> LOG_BLOCK;

    @Override
    public void register() {
        var material = AzaleaMaterial.AZALEA;
        var values = Boat.Type.values(); // TODO: Hack to inject the boat type enums early, fixme.

        WOOD_TYPE = Wood.registerWoodType(material);
        DOOR_BLOCK = Wood.registerDoor(this, material).getFirst();
        TRAPDOOR_BLOCK = Wood.registerTrapdoor(this, material).getFirst();

        var log = Wood.registerLog(this, material);
        LOG_BLOCK = log.get("azalea_log").getFirst(); // Need reference to add to tree feature.

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

        CharmApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        LevelLoadEvent.INSTANCE.handle(this::handleLevelLoad);
    }

    private void handleLevelLoad(MinecraftServer server, ServerLevel level) {
        // Make naturally occurring azalea trees use Charm's azalea log.
        var configuredFeatures = server.registryAccess().registry(Registries.CONFIGURED_FEATURE).orElseThrow();
        ConfiguredFeature<?, ?> feature
            = configuredFeatures.getOrThrow(TreeFeatures.AZALEA_TREE);

        ((ConfiguredFeature<TreeConfiguration, ?>)feature).config().trunkProvider
            = new SimpleStateProvider(LOG_BLOCK.get().defaultBlockState());
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        List<ResourceLocation> remove = new ArrayList<>();

        if (!Charm.LOADER.isEnabled(VariantBarrels.class)) {
            remove.add(Charm.makeId("azalea_barrel"));
        }

        if (!Charm.LOADER.isEnabled(VariantChests.class)) {
            remove.add(Charm.makeId("azalea_chest"));
            remove.add(Charm.makeId("azalea_trapped_chest"));
        }

        if (!Charm.LOADER.isEnabled(VariantLadders.class)) {
            remove.add(Charm.makeId("azalea_ladder"));
        }

        if (!GlobalLoaders.isEnabled(new ResourceLocation("charm_world", "woodcutters"))) {
            remove.add(Charm.makeId("azalea_wood/woodcutting/"));
        }

        return remove;
    }
}
