package svenhjol.charm_core.forge.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;
import svenhjol.charm_api.event.*;
import svenhjol.charm_core.iface.IEvents;
import svenhjol.charm_core.iface.IInitializer;

public class CommonEvents implements IEvents {
    private final IInitializer init;
    private final CommonRegistry registry;
    private final CommonDeferred deferred;
    private static boolean hasInitializedSingletons = false;

    public CommonEvents(IInitializer init, IEventBus modEventBus) {
        this.init = init;
        this.registry = (CommonRegistry)init.getRegistry();
        this.deferred = registry.getDeferred();

        modEventBus.addListener(this::handleRegister);
        modEventBus.addListener(this::handleEntityAttributeCreation);

        if (!hasInitializedSingletons) {
            MinecraftForge.EVENT_BUS.addListener(this::handleEntityJoinLevel);
            MinecraftForge.EVENT_BUS.addListener(this::handleEntityLeaveLevel);
            MinecraftForge.EVENT_BUS.addListener(this::handlePlayerTick);
            MinecraftForge.EVENT_BUS.addListener(this::handlePlayerLoggedIn);
            MinecraftForge.EVENT_BUS.addListener(this::handlePlayerEntityInteract);
            MinecraftForge.EVENT_BUS.addListener(this::handlePlayerBlockRightClick);
            MinecraftForge.EVENT_BUS.addListener(this::handleAttackEntity);
            MinecraftForge.EVENT_BUS.addListener(this::handleLivingDamage);
            MinecraftForge.EVENT_BUS.addListener(this::handleWandererTrades);
            MinecraftForge.EVENT_BUS.addListener(this::handleVillagerTrades);
            MinecraftForge.EVENT_BUS.addListener(this::handleFurnaceFuelBurnTime);
            MinecraftForge.EVENT_BUS.addListener(this::handleBreakSpeed);
            MinecraftForge.EVENT_BUS.addListener(this::handleServerLevelLoad);
            MinecraftForge.EVENT_BUS.addListener(this::handleLootTableLoad);
            hasInitializedSingletons = true;
        }
    }

    public void doFinalTasks() {
        deferred.handleDeferredBlockEntityBlocks();
        deferred.handleDeferredEntitySpawnPlacements();
        deferred.handleDeferredPointOfInterestBlockStates();
        deferred.handleDeferredBlockIgnite();
        deferred.handleDeferredItemFuel();
        deferred.handleDeferredVillagerGifts();
        deferred.handleStrippables();
    }

    public void handleEntityAttributeCreation(EntityAttributeCreationEvent event) {
        deferred.handleDeferredEntityAttributes(event);
    }

    public void handleRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.LOOT_FUNCTION_TYPE)) {
            deferred.handleDeferredLootFunctionTypes();
        }
    }

    private void handleEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel) {
            EntityJoinEvent.INSTANCE.invoke(event.getEntity(), event.getLevel());
        }
    }

    private void handleEntityLeaveLevel(EntityLeaveLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel) {
            EntityLeaveEvent.INSTANCE.invoke(event.getEntity(), event.getLevel());
        }
    }

    private void handleServerLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level) {
            LevelLoadEvent.INSTANCE.invoke(level.getServer(), level);
        }
    }

    private void handlePlayerEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        var hitResult = new EntityHitResult(event.getTarget(), event.getLocalPos());

        var result = EntityUseEvent.INSTANCE.invoke(event.getEntity(), event.getLevel(), event.getHand(), event.getTarget(), hitResult);
        if (result != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    private void handlePlayerBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        var result = BlockUseEvent.INSTANCE.invoke(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
        if (result != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
    
    private void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
        var result = BlockBreakSpeedEvent.INSTANCE.invoke(event.getEntity(), event.getState(), event.getOriginalSpeed());
        event.setNewSpeed(result);
    }

    private void handlePlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PlayerTickEvent.INSTANCE.invoke(event.player);
        }
    }

    private void handlePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerLoginEvent.INSTANCE.invoke(event.getEntity());
    }

    private void handleAttackEntity(AttackEntityEvent event) {
        var hitResult = new EntityHitResult(event.getTarget(), event.getEntity().position());
        var result = EntityAttackEvent.INSTANCE.invoke(event.getEntity(), event.getEntity().getLevel(), event.getEntity().getUsedItemHand(), event.getTarget(), hitResult);
        if (result == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }

    private void handleLivingDamage(LivingDamageEvent event) {
        var result = EntityHurtEvent.INSTANCE.invoke(event.getEntity(), event.getSource(), event.getAmount());
        if (result == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }

    private void handleFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        var item = event.getItemStack().getItem();
        if (deferred.itemFuelMap.containsKey(item)) {
            event.setBurnTime(deferred.itemFuelMap.get(item));
        }
    }

    private void handleLootTableLoad(LootTableLoadEvent event) {
        var lootTable = event.getTable();
        var manager = event.getLootTableManager();
        var id = event.getName();

        LootTableModifyEvent.INSTANCE.invoke(manager, id, pool -> lootTable.addPool(pool.build()));
    }
    
    private void handleWandererTrades(WandererTradesEvent event) {
        deferred.handleDeferredWandererTrades(event);
    }

    private void handleVillagerTrades(VillagerTradesEvent event) {
        deferred.handleDeferredVillagerTrades(event);
    }
}