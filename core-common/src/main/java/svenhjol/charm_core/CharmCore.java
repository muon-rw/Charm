package svenhjol.charm_core;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.iface.*;

public class CharmCore {
    public static final String MOD_ID = "charm_core";
    public static final String PREFIX = "svenhjol.charm_core";
    public static final String FEATURE_PREFIX = PREFIX + ".feature";
    public static ILog LOG;
    public static ILoader LOADER;

    public CharmCore(IInitializer init) {
        LOG = init.getLog();
        LOADER = init.getLoader();

        LOADER.init(FEATURE_PREFIX, Feature.class);
    }

    public void run() {
        LOADER.run();
    }

    public static ResourceLocation makeId(String id) {
        return !id.contains(":") ? new ResourceLocation(MOD_ID, id) : new ResourceLocation(id);
    }
}
