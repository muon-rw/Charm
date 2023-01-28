package svenhjol.charm.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.fabric.base.BaseFabricClientInitializer;
import svenhjol.charm_core.fabric.common.CommonRegistry;

public class FabricClientModInitializer implements ClientModInitializer {
    public static final ClientInitializer INIT = new ClientInitializer();
    private CharmClient mod;

    @Override
    public void onInitializeClient() {
        if (mod == null) {
            // Always init Core first.
            svenhjol.charm_core.fabric.FabricClientModInitializer.initCharmCoreClient();

            mod = new CharmClient(INIT);
            mod.run();
        }
    }

    public static class ClientInitializer extends BaseFabricClientInitializer {
        @Override
        public String getNamespace() {
            return CharmClient.MOD_ID;
        }

        @Override
        public CommonRegistry getCommonRegistry() {
            return FabricModInitializer.INIT.getRegistry();
        }
    }
}
