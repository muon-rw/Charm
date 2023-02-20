package svenhjol.charm_core.forge.registry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public record DeferredBlockRenderType<T extends Block> (
    Supplier<T> block,
    Supplier<RenderType> renderType
) { }
