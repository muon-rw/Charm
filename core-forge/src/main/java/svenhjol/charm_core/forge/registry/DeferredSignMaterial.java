package svenhjol.charm_core.forge.registry;

import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.function.Supplier;

public record DeferredSignMaterial(
    Supplier<WoodType> woodType
) { }
