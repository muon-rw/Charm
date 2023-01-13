package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.iface.IInitializer;
import svenhjol.charm_core.iface.ILoader;
import svenhjol.charm_core.iface.ILog;
import svenhjol.charm_core.iface.IRegistry;

public class Charm {
    public static final String MOD_ID = "charm";
    public static final String PREFIX = "svenhjol.charm";
    public static final String FEATURE_PREFIX = PREFIX + ".feature";
    public static ILog LOG;
    public static ILoader LOADER;
    public static IRegistry REGISTRY;

    public Charm(IInitializer init) {
        LOG = init.getLog();
        LOADER = init.getLoader();
        REGISTRY = init.getRegistry();

        LOADER.init(FEATURE_PREFIX, Feature.class);
    }

    public void run() {
        LOADER.run();
    }

    public static ResourceLocation makeId(String id) {
        return !id.contains(":") ? new ResourceLocation(MOD_ID, id) : new ResourceLocation(id);
    }
}
