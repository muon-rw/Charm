package svenhjol.charm_core.forge.registry;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Supplier;

public record DeferredItemColor(
    ItemColor itemColor,
    List<Supplier<? extends ItemLike>> items
) {
}
