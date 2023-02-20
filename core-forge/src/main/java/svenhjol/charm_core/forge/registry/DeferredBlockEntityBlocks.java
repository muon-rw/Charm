package svenhjol.charm_core.forge.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.function.Supplier;

public record DeferredBlockEntityBlocks<T extends BlockEntity, U extends Block>(
    Supplier<BlockEntityType<T>> supplier,
    List<Supplier<U>> blocks
) {}
