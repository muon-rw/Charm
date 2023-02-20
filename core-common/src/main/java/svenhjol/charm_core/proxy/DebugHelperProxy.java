package svenhjol.charm_core.proxy;

import svenhjol.charm_core.CharmCore;

public class DebugHelperProxy {
    public static boolean isEnabled() {
        return Proxy.INSTANCE.method(CharmCore.PREFIX, "DebugHelper", "isEnabled");
    }
}
