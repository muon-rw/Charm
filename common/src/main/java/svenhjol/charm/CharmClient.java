package svenhjol.charm;

import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.iface.*;

public class CharmClient {
    public static ILog LOG;
    public static ILoader LOADER;
    public static IClientRegistry REGISTRY;

    public CharmClient(IClientInitializer init) {
        LOG = init.getLog();
        LOADER = init.getLoader();
        REGISTRY = init.getRegistry();

        LOADER.init(Charm.FEATURE_PREFIX, ClientFeature.class);
    }

    public void run() {
        LOADER.run();
    }
}
