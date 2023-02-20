package svenhjol.charm_api.iface;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.util.List;

public interface IHasBlockEntityScreens {
    List<Class<? extends AbstractContainerScreen<?>>> getBlockEntityScreens();
}
