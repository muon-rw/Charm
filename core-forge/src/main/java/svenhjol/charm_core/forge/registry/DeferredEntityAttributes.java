package svenhjol.charm_core.forge.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public record DeferredEntityAttributes<T extends Mob>(
    Supplier<EntityType<T>> entity,
    Supplier<AttributeSupplier.Builder> builder
) { }
