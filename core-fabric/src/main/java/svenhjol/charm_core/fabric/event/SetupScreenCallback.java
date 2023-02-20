package svenhjol.charm_core.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.Screen;

public interface SetupScreenCallback {
    Event<SetupScreenCallback> EVENT = EventFactory.createArrayBacked(SetupScreenCallback.class, listeners -> screen -> {
        for (SetupScreenCallback listener : listeners) {
            listener.interact(screen);
        }
    });

    void interact(Screen screen);
}
