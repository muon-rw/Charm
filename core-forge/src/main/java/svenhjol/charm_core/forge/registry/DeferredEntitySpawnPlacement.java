package svenhjol.charm_core.forge.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Supplier;

public record DeferredEntitySpawnPlacement<T extends Mob>(
    Supplier<EntityType<T>> entity,
    SpawnPlacements.Type placementType,
    Heightmap.Types heightmapType,
    SpawnPlacements.SpawnPredicate<T> predicate
) { }
