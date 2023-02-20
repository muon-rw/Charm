package svenhjol.charm_api.iface;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.BiPredicate;

public interface IChecksInventoryItem {
    List<BiPredicate<Player, ItemLike>> getInventoryItemChecks();
}
