package svenhjol.charm.forge;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.forge.base.BaseForgeClientInitializer;

public class ForgeClientModInitializer {
    public static final ClientInitializer INIT = new ClientInitializer();
    private final CharmClient mod;

    public ForgeClientModInitializer() {
        var modEventBus = INIT.getModEventBus();
        modEventBus.addListener(this::handleClientSetup);

        mod = new CharmClient(INIT);
    }

    private void handleClientSetup(FMLClientSetupEvent event) {
        mod.run();
    }

    public static class ClientInitializer extends BaseForgeClientInitializer {
        @Override
        public String getNamespace() {
            return CharmClient.MOD_ID;
        }
    }
}
