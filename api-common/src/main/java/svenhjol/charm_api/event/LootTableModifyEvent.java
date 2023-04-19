package svenhjol.charm_api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;

import java.util.Optional;
import java.util.function.Consumer;

public class LootTableModifyEvent extends CharmEvent<LootTableModifyEvent.Handler> {
    public static final LootTableModifyEvent INSTANCE = new LootTableModifyEvent();

    private LootTableModifyEvent() {}

    public void invoke(LootDataManager manager, ResourceLocation tableId, Consumer<LootPool.Builder> consumer) {
        for (Handler handler : getHandlers()) {
            handler.run(manager, tableId).ifPresent(consumer);
        }
    }

    @FunctionalInterface
    public interface Handler {
        Optional<LootPool.Builder> run(LootDataManager manager, ResourceLocation tableId);
    }
}
