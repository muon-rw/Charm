package svenhjol.charm_api.event;

import net.minecraft.client.gui.screens.Screen;

public class ScreenSetupEvent extends CharmEvent<ScreenSetupEvent.Handler> {
    public static final ScreenSetupEvent INSTANCE = new ScreenSetupEvent();

    private ScreenSetupEvent() {}

    @FunctionalInterface
    public interface Handler {
        void run(Screen screen);
    }
}
