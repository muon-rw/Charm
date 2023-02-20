package svenhjol.charm_core.helper;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import svenhjol.charm_core.mixin.accessor.ScreenAccessor;

public class ScreenHelper {
    public static <T extends GuiEventListener> T addRenderableWidget(Screen screen, T guiEventListener) {
        var wrappedScreen = (ScreenAccessor) screen;
        wrappedScreen.getRenderables().add((Renderable) guiEventListener);
        wrappedScreen.getChildren().add(guiEventListener);
        wrappedScreen.getNarratables().add((NarratableEntry)guiEventListener);
        return guiEventListener;
    }
}
