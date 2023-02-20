package svenhjol.charm_core.fabric.client;

import svenhjol.charm_core.fabric.base.BaseConfig;
import svenhjol.charm_core.iface.IClientInitializer;

public class ClientConfig extends BaseConfig {
    public ClientConfig(IClientInitializer init) {
        super(init.getLog(), init.getNamespace() + "-client");
    }
}
