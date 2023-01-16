package svenhjol.charm.forge;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.forge.base.BaseForgeClientInitializer;

public class ForgeClientModInitializer {
    private final CharmClient mod;

    public static final ClientInitializer INIT = new ClientInitializer();
    public ForgeClientModInitializer() {
        var modEventBus = INIT.getModEventBus();
        modEventBus.addListener(this::handleClientSetup);

        mod = new CharmClient(INIT);
    }

    private void handleClientSetup(FMLClientSetupEvent event) {
        mod.run();

        // Do final registry tasks.
        event.enqueueWork(INIT.getEvents()::doFinalTasks);
    }

    public static class ClientInitializer extends BaseForgeClientInitializer {
        @Override
        public String getNamespace() {
            return Charm.MOD_ID;
        }
    }
}
