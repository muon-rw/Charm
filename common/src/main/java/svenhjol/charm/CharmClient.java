package svenhjol.charm;

import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.iface.IClientInitializer;
import svenhjol.charm_core.iface.ILoader;
import svenhjol.charm_core.iface.ILog;

public class CharmClient {
    public static ILog LOG;
    public static ILoader LOADER;

    public CharmClient(IClientInitializer init) {
        LOG = init.getLog();
        LOADER = init.getLoader();

        LOADER.init(Charm.FEATURE_PREFIX, ClientFeature.class);
    }

    public void run() {
        LOADER.run();
    }
}
