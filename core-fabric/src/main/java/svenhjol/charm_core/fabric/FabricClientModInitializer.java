package svenhjol.charm_core.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm_core.CharmCore;
import svenhjol.charm_core.CharmCoreClient;
import svenhjol.charm_core.fabric.base.BaseFabricClientInitializer;
import svenhjol.charm_core.fabric.common.CommonRegistry;

public class FabricClientModInitializer implements ClientModInitializer {
    private static CharmCoreClient mod;
    public static final ClientInitializer INIT = new ClientInitializer();

    @Override
    public void onInitializeClient() {
        initCharmCoreClient();
    }

    public static void initCharmCoreClient() {
        if (mod == null) {
            mod = new CharmCoreClient(INIT);
            mod.run();
        }
    }

    public static class ClientInitializer extends BaseFabricClientInitializer {
        @Override
        public String getNamespace() {
            return CharmCore.MOD_ID;
        }

        @Override
        public CommonRegistry getCommonRegistry() {
            return FabricModInitializer.INIT.getRegistry();
        }
    }
}
