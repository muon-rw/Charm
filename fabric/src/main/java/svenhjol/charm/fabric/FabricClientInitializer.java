package svenhjol.charm.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;

public class FabricClientInitializer implements ClientModInitializer {
    private CharmClient mod;

    public static final ClientInitializer INIT = new ClientInitializer();

    @Override
    public void onInitializeClient() {
        if (mod == null) {
            mod = new CharmClient(INIT);
            mod.run();
        }
    }

    public static class ClientInitializer extends svenhjol.charm_core.fabric.FabricClientInitializer.ClientInitializer {
        @Override
        public String getNamespace() {
            return Charm.MOD_ID;
        }
    }
}
