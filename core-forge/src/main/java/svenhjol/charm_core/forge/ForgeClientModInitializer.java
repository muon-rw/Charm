package svenhjol.charm_core.forge;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm_core.CharmCore;
import svenhjol.charm_core.CharmCoreClient;
import svenhjol.charm_core.forge.base.BaseForgeClientInitializer;

public class ForgeClientModInitializer {
    private final CharmCoreClient mod;
    public static final ClientInitializer INIT = new ClientInitializer();

    public ForgeClientModInitializer() {
        var modEventBus = INIT.getModEventBus();
        modEventBus.addListener(this::handleClientSetup);

        mod = new CharmCoreClient(INIT);
    }

    private void handleClientSetup(FMLClientSetupEvent event) {
        mod.run();

        event.enqueueWork(INIT.getEvents()::doFinalTasks);
    }

    public static class ClientInitializer extends BaseForgeClientInitializer {
        @Override
        public String getNamespace() {
            return CharmCore.MOD_ID;
        }
    }
}
