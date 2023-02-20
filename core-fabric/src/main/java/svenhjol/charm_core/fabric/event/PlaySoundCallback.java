package svenhjol.charm_core.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

public interface PlaySoundCallback {
    Event<PlaySoundCallback> EVENT = EventFactory.createArrayBacked(PlaySoundCallback.class, (listeners) -> (soundEngine, sound) -> {
        for (PlaySoundCallback listener : listeners) {
            listener.interact(soundEngine, sound);
        }
    });

    void interact(SoundEngine soundEngine, SoundInstance sound);
}
