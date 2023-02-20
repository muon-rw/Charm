package svenhjol.charm_core.forge.registry;

import svenhjol.charm_core.iface.IFuelProvider;

import java.util.function.Supplier;

public record DeferredItemFuel<T extends IFuelProvider>(
    Supplier<T> provider
) { }
