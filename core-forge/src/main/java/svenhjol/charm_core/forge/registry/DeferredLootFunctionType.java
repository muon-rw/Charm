package svenhjol.charm_core.forge.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.util.function.Supplier;

public record DeferredLootFunctionType(
    ResourceLocation id,
    Supplier<LootItemFunctionType> supplier
) { }
