package svenhjol.charm.feature.item_frame_hiding.client;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHidingClient;
import svenhjol.charm.feature.item_frame_hiding.common.Networking;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<ItemFrameHidingClient> {
    public final Supplier<ParticleEngine.SpriteParticleRegistration<SimpleParticleType>> particle;

    public Registers(ItemFrameHidingClient feature) {
        super(feature);
        var registry = feature.registry();

        particle = registry.particle(feature().linked().registers.particleType,
            () -> Particle::new);

        registry.packetReceiver(Networking.S2CAddAmethyst.TYPE,
            () -> feature().handlers::addToItemFrame);
        registry.packetReceiver(Networking.S2CRemoveAmethyst.TYPE,
            () -> feature().handlers::removeFromItemFrame);
    }
}
