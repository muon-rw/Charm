package svenhjol.charm_api.iface;

import net.minecraft.world.level.ItemLike;

import java.util.List;

public interface IHoverSorting {
    /**
     * List of items that allow hover sorting.
     */
    List<ItemLike> getSortables();
}
