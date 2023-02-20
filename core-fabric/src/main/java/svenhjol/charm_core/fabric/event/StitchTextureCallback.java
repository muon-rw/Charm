package svenhjol.charm_core.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public interface StitchTextureCallback {
    Event<StitchTextureCallback> EVENT = EventFactory.createArrayBacked(StitchTextureCallback.class, listeners -> (atlas, addSprite) -> {
        for (StitchTextureCallback listener : listeners) {
            listener.interact(atlas, addSprite);
        }
    });

    void interact(TextureAtlas atlas, Function<ResourceLocation, Boolean> addSprite);
}
