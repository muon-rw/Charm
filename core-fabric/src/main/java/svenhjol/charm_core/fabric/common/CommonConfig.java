package svenhjol.charm_core.fabric.common;

import svenhjol.charm_core.fabric.base.BaseConfig;
import svenhjol.charm_core.iface.IInitializer;

public class CommonConfig extends BaseConfig {
    public CommonConfig(IInitializer init) {
        super(init.getLog(), init.getNamespace() + "-common");
    }
}
