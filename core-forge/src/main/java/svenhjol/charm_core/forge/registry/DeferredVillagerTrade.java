package svenhjol.charm_core.forge.registry;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.function.Supplier;

public record DeferredVillagerTrade(
    Supplier<VillagerProfession> profession,
    Supplier<VillagerTrades.ItemListing> trade,
    int tier
) { }
