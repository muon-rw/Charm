package svenhjol.charm.feature.portable_crafting;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.portable_crafting.PortableCraftingNetwork.OpenCrafting;
import svenhjol.charm_api.iface.IChecksInventoryTag;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.helper.TextHelper;
import svenhjol.charm_core.init.AdvancementHandler;
import svenhjol.charm_core.init.CharmApi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiPredicate;

@Feature(mod = Charm.MOD_ID, description = "Allows crafting from inventory if the player has a crafting table in their inventory.")
public class PortableCrafting extends CharmFeature implements IChecksInventoryTag {
    private static final ResourceLocation ADVANCEMENT = Charm.makeId("used_portable_crafting_table");
    private static final Component CONTAINER_LABEL = TextHelper.translatable("container.charm.portable_crafting_table");
    private static final int CHECK_TICKS_FAST = 5;
    private static final List<UUID> CACHED_HAS_CRAFTING_TABLE = new ArrayList<>();
    public static TagKey<Item> CRAFTING_TABLES;

    @Override
    public void register() {
        PortableCraftingNetwork.register();
        CharmApi.registerProvider(this);

        CRAFTING_TABLES = TagKey.create(BuiltInRegistries.ITEM.key(), Charm.makeId("crafting_tables"));
    }

    public static boolean hasCraftingTable(Player player) {
        var uuid = player.getUUID();

        if (!CACHED_HAS_CRAFTING_TABLE.contains(uuid) || player.level.getGameTime() % CHECK_TICKS_FAST == 0) {
            var hasCraftingTable = CharmApi.getProviders(IChecksInventoryTag.class).stream()
                .flatMap(s -> s.getInventoryTagChecks().stream())
                .anyMatch(check -> check.test(player, CRAFTING_TABLES));

            CACHED_HAS_CRAFTING_TABLE.remove(uuid);

            if (hasCraftingTable) {
                CACHED_HAS_CRAFTING_TABLE.add(uuid);
            }
        }

        return CACHED_HAS_CRAFTING_TABLE.contains(uuid);
    }

    public static void openContainer(ServerPlayer player) {
        player.closeContainer();
        player.openMenu(new SimpleMenuProvider((i, inv, p) -> new PortableCraftingMenu(i, inv, ContainerLevelAccess.create(p.level, p.blockPosition())), CONTAINER_LABEL));
    }

    public static void handleOpenedCraftingTable(OpenCrafting message, Player player) {
        if (!hasCraftingTable(player)) return;
        var serverPlayer = (ServerPlayer) player;

        AdvancementHandler.trigger(ADVANCEMENT, serverPlayer);
        openContainer(serverPlayer);
    }


    @Override
    public List<BiPredicate<Player, TagKey<Item>>> getInventoryTagChecks() {
        return List.of(
            (player, itemTag) -> {
                List<ItemStack> items = new ArrayList<>();
                var inventory = player.getInventory();
                items.addAll(inventory.items);
                items.addAll(inventory.offhand);
                return items.stream().anyMatch(stack -> stack.is(itemTag));
            }
        );
    }
}
