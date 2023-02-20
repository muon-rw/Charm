package svenhjol.charm_api.event;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

public class SoundPlayEvent extends CharmEvent<SoundPlayEvent.Handler> {
    public static final SoundPlayEvent INSTANCE = new SoundPlayEvent();

    private SoundPlayEvent() {}

    public void invoke(SoundEngine soundEngine, SoundInstance soundInstance) {
        for (Handler handler : getHandlers()) {
            handler.run(soundEngine, soundInstance);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(SoundEngine soundEngine, SoundInstance soundInstance);
    }
}
