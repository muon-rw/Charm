package svenhjol.charm_core.forge.registry;

import svenhjol.charm_core.iface.IIgniteProvider;

import java.util.function.Supplier;

public record DeferredBlockIgnite<T extends IIgniteProvider>(
    Supplier<T> provider
) { }
