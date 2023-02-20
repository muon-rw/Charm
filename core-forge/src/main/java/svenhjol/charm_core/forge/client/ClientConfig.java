package svenhjol.charm_core.forge.client;

import net.minecraftforge.fml.config.ModConfig;
import svenhjol.charm_core.forge.base.BaseConfig;
import svenhjol.charm_core.iface.IClientInitializer;

public class ClientConfig extends BaseConfig {
    public ClientConfig(IClientInitializer init) {
        super(init.getLog(), ModConfig.Type.CLIENT);
    }
}
