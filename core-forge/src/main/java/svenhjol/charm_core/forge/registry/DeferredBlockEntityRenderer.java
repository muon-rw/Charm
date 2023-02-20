package svenhjol.charm_core.forge.registry;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public record DeferredBlockEntityRenderer<T extends BlockEntity>(
    Supplier<BlockEntityType<T>> type,
    Supplier<BlockEntityRendererProvider<T>> provider
) {}
