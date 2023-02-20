package svenhjol.charm_core.forge.registry;

import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public record DeferredParticleProvider(
    Supplier<SimpleParticleType> particleType,
    Supplier<SpriteParticleRegistration<SimpleParticleType>> particleProvider
) { }
