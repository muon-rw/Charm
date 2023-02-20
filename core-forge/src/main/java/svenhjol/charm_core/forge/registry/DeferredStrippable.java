package svenhjol.charm_core.forge.registry;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public record DeferredStrippable(
    Supplier<? extends Block> block,
    Supplier<? extends Block> strippedBlock
) { }
