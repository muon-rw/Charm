package svenhjol.charm.forge;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.CharmClient;

public class ForgeClientInitializer {
    private final CharmClient mod;

    public static final ClientInitializer INIT = new ClientInitializer();
    public ForgeClientInitializer() {
        var modEventBus = INIT.getModEventBus();
        modEventBus.addListener(this::handleClientSetup);

        mod = new CharmClient(INIT);
    }

    private void handleClientSetup(FMLClientSetupEvent event) {
        mod.run();

        // Do final registry tasks.
        event.enqueueWork(INIT.getEvents()::doFinalTasks);
    }

    public static class ClientInitializer extends svenhjol.charm_core.forge.ForgeClientInitializer.ClientInitializer { }
}
