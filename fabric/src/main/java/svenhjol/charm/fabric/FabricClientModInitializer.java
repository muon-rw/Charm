package svenhjol.charm.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.Charm;
import svenhjol.charm_core.fabric.base.BaseFabricClientInitializer;
import svenhjol.charm_core.fabric.common.CommonRegistry;

public class FabricClientModInitializer implements ClientModInitializer {
    public static final ClientInitializer INIT = new ClientInitializer();
    private Charm mod;

    @Override
    public void onInitializeClient() {
        // Always init Core first.
        svenhjol.charm_core.fabric.FabricClientModInitializer.initCharmCoreClient();

        if (mod == null) {
            mod = new Charm(INIT);
            mod.run();
        }
    }

    public static class ClientInitializer extends BaseFabricClientInitializer {
        @Override
        public CommonRegistry getCommonRegistry() {
            return null; // No common registry with client-only mods.
        }

        @Override
        public String getNamespace() {
            return Charm.MOD_ID;
        }
    }
}
