package svenhjol.charm_core;

import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.iface.IClientInitializer;
import svenhjol.charm_core.iface.ILoader;
import svenhjol.charm_core.iface.ILog;

public class CharmCoreClient {
    public static ILog LOG;
    public static ILoader LOADER;

    public CharmCoreClient(IClientInitializer init) {
        LOG = init.getLog();
        LOADER = init.getLoader();

        LOADER.init(CharmCore.FEATURE_PREFIX, ClientFeature.class);
    }

    public void run() {
        LOADER.run();
    }
}
