package svenhjol.charm.init;

import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.module.core.Core;

public class CharmDebug {
    public static void init() {
        var toml = ConfigHelper.readConfig(Charm.MOD_ID);
        Core.debug = ConfigHelper.isDebugMode(toml) || FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static boolean isEnabled() {
        return Core.debug;
    }
}
