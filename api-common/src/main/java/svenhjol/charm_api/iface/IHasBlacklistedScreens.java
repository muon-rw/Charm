package svenhjol.charm_api.iface;

import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public interface IHasBlacklistedScreens {
    List<Class<? extends Screen>> getBlacklistedScreens();
}
