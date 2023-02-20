package svenhjol.charm_core.forge.registry;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public record DeferredEntityRenderer<T extends Entity>(
    Supplier<EntityType<T>> entity,
    Supplier<EntityRendererProvider<T>> provider
) { }
