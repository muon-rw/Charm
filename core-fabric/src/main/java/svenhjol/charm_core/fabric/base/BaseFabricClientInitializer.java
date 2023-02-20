package svenhjol.charm_core.fabric.base;

import svenhjol.charm_core.fabric.Log;
import svenhjol.charm_core.fabric.client.*;
import svenhjol.charm_core.fabric.common.CommonRegistry;
import svenhjol.charm_core.iface.IClientInitializer;

import java.util.Optional;

public abstract class BaseFabricClientInitializer implements IClientInitializer {
    private final Log log;
    private final ClientConfig config;
    private final ClientRegistry registry;
    private final ClientEvents events;
    private final ClientLoader loader;
    private final ClientNetwork network;

    public BaseFabricClientInitializer() {
        log = new Log(getNamespace());
        config = new ClientConfig(this);
        registry = new ClientRegistry(this);
        events = new ClientEvents(this);
        loader = new ClientLoader(this, config);
        network = new ClientNetwork();

        // Do late registration of all client network packets.
        Optional.ofNullable(getCommonRegistry()).ifPresent(registry::handleDeferredClientPackets);
    }

    public ClientConfig getConfig() {
        return config;
    }

    /**
     * A reference to the current mod's CommonRegistry.
     * You should override this in the custom mod.
     * @return current mod's CommonRegistry instance.
     */
    public abstract CommonRegistry getCommonRegistry();

    @Override
    public String getRegistryNamespace() {
        return getNamespace();
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public ClientEvents getEvents() {
        return events;
    }

    @Override
    public ClientRegistry getRegistry() {
        return registry;
    }

    @Override
    public ClientNetwork getNetwork() {
        return network;
    }

    @Override
    public ClientLoader getLoader() {
        return loader;
    }
}
