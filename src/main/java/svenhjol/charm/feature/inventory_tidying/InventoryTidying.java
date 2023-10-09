package svenhjol.charm.feature.inventory_tidying;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.inventory_tidying.InventoryTidyingNetwork.TidyInventory;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;

@Feature(mod = Charm.MOD_ID, description = "Button to automatically tidy inventories.")
public class InventoryTidying extends CharmonyFeature {
    @Override
    public void runWhenEnabled() {
        InventoryTidyingHandler.init();
        InventoryTidyingNetwork.register();
    }

    public static void handleTidyInventory(TidyInventory message, Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (player.isSpectator()) return;

        var type = message.getType();
        AbstractContainerMenu useContainer;

        switch (type) {
            case PLAYER -> useContainer = serverPlayer.inventoryMenu;
            case CONTAINER -> useContainer = serverPlayer.containerMenu;
            default -> {
                return;
            }
        }

        var slots = useContainer.slots;
        var hasItemsInContainer = false;

        for (var slot : slots) {
            var inventory = slot.container;

            if (type == TidyType.PLAYER && slot.container == serverPlayer.getInventory()) {
                InventoryTidyingHandler.sort(player.getInventory(), 9, 36);
                hasItemsInContainer = !slot.container.isEmpty();
                break;
            } else if (type == TidyType.CONTAINER) {
                InventoryTidyingHandler.sort(inventory, 0, inventory.getContainerSize());
                hasItemsInContainer = !slot.container.isEmpty();
                break;
            }
        }

        if (hasItemsInContainer) {
            triggerTidiedInventory(player);
        }
    }

    public static void triggerTidiedInventory(Player player) {
        Advancements.trigger(Charm.instance().makeId("tidied_inventory"), player);
    }
}
