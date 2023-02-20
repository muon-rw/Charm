package svenhjol.charm_api.event;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class TextureStitchEvent extends CharmEvent<TextureStitchEvent.Handler> {
    public static final TextureStitchEvent INSTANCE = new TextureStitchEvent();

    private TextureStitchEvent() {}

    @FunctionalInterface
    public interface Handler {
        void run(TextureAtlas atlas, Function<ResourceLocation, Boolean> addTexture);
    }
}
