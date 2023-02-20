package svenhjol.charm_core.proxy;

import svenhjol.charm_core.CharmCore;

import java.nio.file.Path;
import java.util.List;

public class ConfigHelperProxy {
    public static boolean isModLoaded(String modName) {
        return Proxy.INSTANCE.method(CharmCore.PREFIX,
            "ConfigHelper",
            "isModLoaded",
            List.of(modName),
            List.of(String.class));
    }

    public static boolean isFeatureEnabled(String modId, String featureName) {
        return Proxy.INSTANCE.method(CharmCore.PREFIX,
            "ConfigHelper",
            "isFeatureEnabled",
            List.of(modId, featureName),
            List.of(String.class, String.class));
    }

    public static boolean configExists(String modId) {
        return Proxy.INSTANCE.method(CharmCore.PREFIX,
            "ConfigHelper",
            "configExists",
            List.of(modId),
            List.of(String.class));
    }

    public static Path getConfigDir() {
        return Proxy.INSTANCE.method(CharmCore.PREFIX,
            "ConfigHelper",
            "getConfigDir");
    }
}
