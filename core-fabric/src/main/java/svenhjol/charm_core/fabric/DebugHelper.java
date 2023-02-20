package svenhjol.charm_core.fabric;

import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charm_core.CharmCore;

public class DebugHelper {
    private static boolean hasChecked = false;
    private static boolean cachedValue = false;

    public static boolean isEnabled() {
        var isDevEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment();
        if (isDevEnvironment) return true;

        if (!hasChecked) {
            var toml = ConfigHelper.read(CharmCore.MOD_ID);
            var key = "\"Debug mode\"";
            if (toml.contains(key)) {
                cachedValue = toml.getBoolean(key);
            }

            hasChecked = true;
        }

        return cachedValue;
    }
}
