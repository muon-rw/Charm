package svenhjol.charm_core.forge.registry;

import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public record DeferredRecipeCategoryEnum(
    String name,
    Supplier<? extends ItemLike> menuIcon
) { }
