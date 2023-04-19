package svenhjol.charm_core.fabric.common;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm_api.event.*;
import svenhjol.charm_core.fabric.event.BlockBreakSpeedCallback;
import svenhjol.charm_core.fabric.event.PlayerLoginCallback;
import svenhjol.charm_core.fabric.event.PlayerTickCallback;
import svenhjol.charm_core.iface.IEvents;
import svenhjol.charm_core.iface.IInitializer;

public class CommonEvents implements IEvents {
    private final IInitializer init;
    private final CommonRegistry registry;
    private static boolean hasInitializedSingletons = false;

    public CommonEvents(IInitializer init) {
        this.init = init;
        this.registry = (CommonRegistry) init.getRegistry();

        if (!hasInitializedSingletons) {
            ServerEntityEvents.ENTITY_LOAD.register(this::handleServerEntityLoad);
            ServerEntityEvents.ENTITY_UNLOAD.register(this::handleServerEntityUnload);
            PlayerLoginCallback.EVENT.register(this::handlePlayerLogin);
            PlayerTickCallback.EVENT.register(this::handlePlayerTick);
            AttackEntityCallback.EVENT.register(this::handleAttackEntity);
            UseEntityCallback.EVENT.register(this::handleUseEntity);
            UseBlockCallback.EVENT.register(this::handleUseBlock);
            BlockBreakSpeedCallback.EVENT.register(this::handleBlockBreakSpeed);
            ServerWorldEvents.LOAD.register(this::handleServerWorldLoad);
            LootTableEvents.MODIFY.register(this::handleLootTableModify);

            hasInitializedSingletons = true;
        }
    }

    private InteractionResult handleAttackEntity(Player player, Level level, InteractionHand handle, Entity entity, @Nullable EntityHitResult hitResult) {
        return EntityAttackEvent.INSTANCE.invoke(player, level, handle, entity, hitResult);
    }

    private void handleLootTableModify(ResourceManager resourceManager, LootDataManager lootTables, ResourceLocation id, LootTable.Builder supplier, LootTableSource lootTableSource) {
        LootTableModifyEvent.INSTANCE.invoke(lootTables, id, supplier::withPool);
    }

    private void handlePlayerTick(Player player) {
        PlayerTickEvent.INSTANCE.invoke(player);
    }

    private void handlePlayerLogin(Player player) {
        PlayerLoginEvent.INSTANCE.invoke(player);
    }

    private InteractionResult handleUseEntity(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        return EntityUseEvent.INSTANCE.invoke(player, level, hand, entity, hitResult);
    }

    private InteractionResult handleUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return BlockUseEvent.INSTANCE.invoke(player, level, hand, hitResult);
    }
    
    private float handleBlockBreakSpeed(Player player, BlockState state, float originalSpeed) {
        return BlockBreakSpeedEvent.INSTANCE.invoke(player, state, originalSpeed);
    }

    private void handleServerWorldLoad(MinecraftServer server, ServerLevel level) {
        LevelLoadEvent.INSTANCE.invoke(server, level);
    }
    
    private void handleServerEntityLoad(Entity entity, ServerLevel serverLevel) {
        EntityJoinEvent.INSTANCE.invoke(entity, serverLevel);
    }

    private void handleServerEntityUnload(Entity entity, Level level) {
        EntityLeaveEvent.INSTANCE.invoke(entity, level);
    }
}