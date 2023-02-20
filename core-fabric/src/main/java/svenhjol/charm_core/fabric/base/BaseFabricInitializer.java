package svenhjol.charm_core.fabric.base;

import svenhjol.charm_core.fabric.Log;
import svenhjol.charm_core.fabric.common.CommonConfig;
import svenhjol.charm_core.fabric.common.CommonEvents;
import svenhjol.charm_core.fabric.common.CommonLoader;
import svenhjol.charm_core.fabric.common.CommonRegistry;
import svenhjol.charm_core.fabric.server.ServerNetwork;
import svenhjol.charm_core.iface.IInitializer;

public abstract class BaseFabricInitializer implements IInitializer {
    private final Log log;
    private final CommonConfig config;
    private final CommonRegistry registry;
    private final CommonEvents events;
    private final CommonLoader loader;
    private final ServerNetwork network;

    public BaseFabricInitializer() {
        log = new Log(getNamespace());
        config = new CommonConfig(this);
        registry = new CommonRegistry(this);
        events = new CommonEvents(this);
        loader = new CommonLoader(this, config);
        network = new ServerNetwork();
    }

    public BaseConfig getConfig() {
        return config;
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
    public CommonEvents getEvents() {
        return events;
    }

    @Override
    public CommonRegistry getRegistry() {
        return registry;
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
