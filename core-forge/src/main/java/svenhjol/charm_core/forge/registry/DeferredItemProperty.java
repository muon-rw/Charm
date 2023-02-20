package svenhjol.charm_core.forge.registry;

import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public record DeferredItemProperty(
    ResourceLocation id,
    Supplier<? extends Item> item,
    Supplier<ItemPropertyFunction> function
) { }
