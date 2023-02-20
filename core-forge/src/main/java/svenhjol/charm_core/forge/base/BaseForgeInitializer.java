package svenhjol.charm_core.forge.base;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import svenhjol.charm_core.forge.Log;
import svenhjol.charm_core.forge.common.CommonConfig;
import svenhjol.charm_core.forge.common.CommonEvents;
import svenhjol.charm_core.forge.common.CommonLoader;
import svenhjol.charm_core.forge.common.CommonRegistry;
import svenhjol.charm_core.forge.server.ServerNetwork;
import svenhjol.charm_core.iface.IInitializer;
import svenhjol.charm_core.iface.ILog;

public abstract class BaseForgeInitializer implements IInitializer {
    private final Log log;
    private final BaseConfig config;
    private final CommonRegistry registry;
    private final CommonLoader loader;
    private final CommonEvents events;
    private final ServerNetwork network;
    private final IEventBus modEventBus;

    public BaseForgeInitializer() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        log = new Log();
        config = new CommonConfig(this);
        registry = new CommonRegistry(this);
        loader = new CommonLoader(this, config);
        events = new CommonEvents(this, modEventBus);
        network = new ServerNetwork();

        // Listen to Forge config changes.
        modEventBus.addListener(config::refresh);
    }

    public IEventBus getModEventBus() {
        return modEventBus;
    }

    public BaseConfig getConfig() {
        return config;
    }

    @Override
    public String getRegistryNamespace() {
        return getNamespace();
    }

    @Override
    public ILog getLog() {
        return log;
    }

    @Override
    public CommonRegistry getRegistry() {
        return registry;
    }

    public CommonEvents getEvents() {
        return events;
    }

    @Override
    public CommonLoader getLoader() {
        return loader;
    }

    @Override
    public ServerNetwork getNetwork() {
        return network;
    }
}
