package svenhjol.charm_core.forge.registry;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

public record DeferredBlockColor(
    BlockColor blockColor,
    List<Supplier<? extends Block>> blocks
) { }
