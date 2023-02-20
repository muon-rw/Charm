package svenhjol.charm_core.forge.base;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import svenhjol.charm_core.forge.Log;
import svenhjol.charm_core.forge.client.*;
import svenhjol.charm_core.iface.IClientInitializer;

public abstract class BaseForgeClientInitializer implements IClientInitializer {
    private final Log log;
    private final BaseConfig config;
    private final ClientRegistry registry;
    private final ClientLoader loader;
    private final ClientEvents events;
    private final ClientNetwork network;
    private final IEventBus modEventBus;

    public BaseForgeClientInitializer() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        log = new Log();
        config = new ClientConfig(this);
        registry = new ClientRegistry(this);
        loader = new ClientLoader(this, config);
        events = new ClientEvents(this, modEventBus);
        network = new ClientNetwork();
    }

    public IEventBus getModEventBus() {
        return modEventBus;
    }

    @Override
    public String getRegistryNamespace() {
        return getNamespace();
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public ClientRegistry getRegistry() {
        return registry;
    }

    @Override
    public ClientEvents getEvents() {
        return events;
    }

    @Override
    public ClientLoader getLoader() {
        return loader;
    }

    @Override
    public ClientNetwork getNetwork() {
        return network;
    }
}
