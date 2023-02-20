package svenhjol.charm_core.forge;

import svenhjol.charm_core.CharmCore;

public class DebugHelper {
    private static boolean hasChecked = false;
    private static boolean cachedValue = false;

    public static boolean isEnabled() {
        if (!hasChecked) {
            ConfigHelper.readConfigFile(CharmCore.MOD_ID);
            var lines = ConfigHelper.getConfigLines();

            for (var line : lines) {
                if (line.contains("Debug mode")) {
                    cachedValue = line.contains("true");
                }
            }

            hasChecked = true;
        }

        return cachedValue;
    }
}
