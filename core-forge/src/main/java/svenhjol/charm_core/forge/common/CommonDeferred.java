package svenhjol.charm_core.forge.common;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.charm_core.forge.registry.*;
import svenhjol.charm_core.iface.IFuelProvider;
import svenhjol.charm_core.iface.IIgniteProvider;
import svenhjol.charm_core.mixin.accessor.AxeItemAccessor;
import svenhjol.charm_core.mixin.accessor.BlockEntityTypeAccessor;
import svenhjol.charm_core.mixin.accessor.FireBlockAccessor;

import java.util.*;
import java.util.function.Supplier;

public class CommonDeferred {
    public final List<DeferredBlockEntityBlocks<BlockEntity, Block>> blockEntityBlocks = new ArrayList<>();
    public final List<DeferredWandererTrade> wandererTrades = new ArrayList<>();
    public final List<DeferredVillagerTrade> villagerTrades = new ArrayList<>();
    public final List<DeferredEntitySpawnPlacement<Mob>> entitySpawnPlacements = new ArrayList<>();
    public final List<DeferredEntityAttributes<Mob>> entityAttributes = new ArrayList<>();
    public final List<DeferredPointOfInterestBlockStates> pointOfInterestBlockStates = new ArrayList<>();
    public final List<DeferredBlockIgnite<IIgniteProvider>> blockIgnites = new ArrayList<>();
    public final List<DeferredItemFuel<IFuelProvider>> itemFuels = new ArrayList<>();
    public final List<DeferredVillagerGift> villagerGifts = new ArrayList<>();
    public final List<DeferredLootFunctionType> lootFunctionTypes = new ArrayList<>();
    public final List<DeferredStrippable> strippables = new ArrayList<>();
    public final Map<BlockState, Holder<PoiType>> blockstatePoitMap = new HashMap<>();
    public final Map<ItemLike, Integer> itemFuelMap = new HashMap<>();

    public void handleDeferredBlockEntityBlocks()  {
        for (var deferred : blockEntityBlocks) {
            var blockEntityBlocks = ((BlockEntityTypeAccessor) deferred.supplier().get()).getValidBlocks();
            List<Block> mutable = new ArrayList<>(blockEntityBlocks);

            for (Supplier<? extends Block> blockSupplier : deferred.blocks()) {
                var block = blockSupplier.get();
                if (!mutable.contains(block)) {
                    mutable.add(block);
                }
            }

            ((BlockEntityTypeAccessor) deferred.supplier().get()).setValidBlocks(new HashSet<>(mutable));
        }
    }

    public void handleDeferredWandererTrades(WandererTradesEvent event) {
        for (var deferred : wandererTrades) {
            var itemListing = deferred.itemListing().get();

            if (deferred.isRare()) {
                event.getRareTrades().add(itemListing);
            } else {
                event.getGenericTrades().add(itemListing);
            }
        }
    }

    public void handleDeferredEntitySpawnPlacements() {
        for (var deferred : entitySpawnPlacements) {
            SpawnPlacements.register(
                deferred.entity().get(),
                deferred.placementType(),
                deferred.heightmapType(),
                deferred.predicate());
        }
    }

    public void handleDeferredEntityAttributes(EntityAttributeCreationEvent event) {
        for (var deferred : entityAttributes) {
            event.put(deferred.entity().get(), deferred.builder().get().build());
        }
    }

    public void handleDeferredPointOfInterestBlockStates() {
        for (var deferred : pointOfInterestBlockStates) {
            var resourceKey = deferred.resourceKey().get();
            var blockStates = deferred.blockStates().get();
            var holder = ForgeRegistries.POI_TYPES.getHolder(resourceKey);

            if (holder.isEmpty()) {
                continue;
            }

            for (var state : blockStates) {
                blockstatePoitMap.put(state, holder.get());
            }
        }
    }

    public void handleDeferredBlockIgnite() {
        for (var deferred : blockIgnites) {
            var provider = deferred.provider().get();
            ((FireBlockAccessor) Blocks.FIRE).invokeSetFlammable(
                (Block)provider, provider.igniteChance(), provider.burnChance()
            );
        }
    }

    public void handleDeferredItemFuel() {
        for (var deferred : itemFuels) {
            var provider = deferred.provider().get();
            var fuelTime = provider.fuelTime();

            if (provider instanceof Item item) {
                itemFuelMap.put(item, fuelTime);
            }
        }
    }

    public void handleDeferredLootFunctionTypes() {
        for (var deferred : lootFunctionTypes) {
            var id = deferred.id();
            var supplier = deferred.supplier();
            Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, id, supplier.get());
        }
    }

    public void handleDeferredVillagerGifts() {
        for (var deferred : villagerGifts) {
            var id = deferred.id();
            var holder = ForgeRegistries.VILLAGER_PROFESSIONS.getHolder(id).orElseThrow();
            var lootTable = new ResourceLocation(id.getNamespace(), "gameplay/hero_of_the_village/" + id.getPath() + "_gift");
            GiveGiftToHero.GIFTS.put(holder.value(), lootTable);
        }
    }

    public void handleDeferredVillagerTrades(VillagerTradesEvent event) {
        for (var deferred : villagerTrades) {
            if (event.getType() != deferred.profession().get()) continue;
            event.getTrades().get(deferred.tier()).add(deferred.trade().get());
        }
    }

    public void handleStrippables() {
        // Make axe strippables map mutable.
        var strippables = AxeItemAccessor.getStrippables();
        AxeItemAccessor.setStrippables(new HashMap<>(strippables));

        for (var deferred : this.strippables) {
            AxeItemAccessor.getStrippables().put(deferred.block().get(), deferred.strippedBlock().get());
        }
    }
}
