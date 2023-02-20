package svenhjol.charm_core.forge.common;

import net.minecraftforge.fml.config.ModConfig;
import svenhjol.charm_core.forge.base.BaseConfig;
import svenhjol.charm_core.iface.IInitializer;

public class CommonConfig extends BaseConfig {
    public CommonConfig(IInitializer init) {
        super(init.getLog(), ModConfig.Type.COMMON);
    }
}
