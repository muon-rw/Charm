package svenhjol.charm_core.forge.registry;

import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.function.Supplier;

public record DeferredWandererTrade(
    Supplier<VillagerTrades.ItemListing> itemListing,
    boolean isRare
) {}
