package svenhjol.charm.feature.mob_textures.client;

import net.minecraft.world.entity.EntityType;
import svenhjol.charm.api.event.ClientEntityJoinEvent;
import svenhjol.charm.feature.mob_textures.MobTextures;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<MobTextures> {
    public Registers(MobTextures feature) {
        super(feature);
        var registry = feature.registry();

        if (feature().chickens()) {
            registry.entityRenderer(() -> EntityType.CHICKEN,
                () -> Renderer.RenderChicken::new);
        }

        if (feature().cows()) {
            registry.entityRenderer(() -> EntityType.COW,
                () -> Renderer.RenderCow::new);
        }

        if (feature().dolphins()) {
            registry.entityRenderer(() -> EntityType.DOLPHIN,
                () -> Renderer.RenderDolphin::new);
        }

        if (feature().pigs()) {
            registry.entityRenderer(() -> EntityType.PIG,
                () -> Renderer.RenderPig::new);
        }

        if (feature().sheep()) {
            registry.entityRenderer(() -> EntityType.SHEEP,
                () -> Renderer.RenderSheep::new);
        }

        if (feature().snowGolems()) {
            registry.entityRenderer(() -> EntityType.SNOW_GOLEM,
                () -> Renderer.RenderSnowGolem::new);
        }

        if (feature().squids()) {
            registry.entityRenderer(() -> EntityType.SQUID,
                () -> Renderer.RenderSquid::new);
        }

        if (feature().turtles()) {
            registry.entityRenderer(() -> EntityType.TURTLE,
                () -> Renderer.RenderTurtle::new);
        }

        if (feature().wanderingTraders()) {
            registry.entityRenderer(() -> EntityType.WANDERING_TRADER,
                () -> Renderer.RenderWanderingTrader::new);
        }
    }

    @Override
    public void onEnabled() {
        ClientEntityJoinEvent.INSTANCE.handle(feature().handlers::playerJoin);
    }
}