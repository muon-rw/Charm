package svenhjol.charm_api.iface;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.BiFunction;

public interface IGetsInventoryItem {
    /**
     * If the player has the specified item, return the first one found.
     */
    List<BiFunction<Player, ItemLike, ItemStack>> getInventoryItem();
}
