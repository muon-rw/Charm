package svenhjol.charm_core.forge.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

public record DeferredPointOfInterestBlockStates(
    Supplier<ResourceKey<PoiType>> resourceKey,
    Supplier<List<BlockState>> blockStates
) { }
