package svenhjol.charm_core.forge.registry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public record DeferredMenuScreen<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>>(
    Supplier<MenuType<T>> menuType,
    Supplier<MenuScreens.ScreenConstructor<T, U>> screenConstructor
) { }
