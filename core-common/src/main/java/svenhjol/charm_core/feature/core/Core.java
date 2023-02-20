package svenhjol.charm_core.feature.core;

import svenhjol.charm_core.CharmCore;
import svenhjol.charm_core.annotation.Configurable;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

@Feature(mod = CharmCore.MOD_ID, switchable = false, priority = 50)
public class Core extends CharmFeature {
    @Configurable(name = "Debug mode", description = "Enable debugging mode. Produces more logging output and adds some testing code.")
    public static boolean debug = false;
}
