package svenhjol.charm_core.forge.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Holds an item and the tab it should appear under.
 * @param item The item to display.
 * @param tab The tab to display the item in.
 * @param after Display after this item in the tab.
 */
public record DeferredItemTab(
    Supplier<Item> item,
    CreativeModeTab tab,
    @Nullable ItemLike after
) {
}
