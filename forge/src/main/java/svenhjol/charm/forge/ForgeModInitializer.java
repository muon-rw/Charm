package svenhjol.charm.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm_core.forge.base.BaseForgeInitializer;

@Mod(Charm.MOD_ID)
public class ForgeModInitializer {
    private final Charm mod;

    public static final Initializer INIT = new Initializer();

    public ForgeModInitializer() {
        var modEventBus = INIT.getModEventBus();
        modEventBus.addListener(this::handleCommonSetup);

        mod = new Charm(INIT);

        // Execute client init so that client registration happens.
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ForgeClientModInitializer::new);

        // Add all the registers to the Forge event bus.
        INIT.getRegistry().register(modEventBus);
    }

    private void handleCommonSetup(FMLCommonSetupEvent event) {
        mod.run();

        // Do final registry tasks.
        event.enqueueWork(INIT.getEvents()::doFinalTasks);
    }

    public static class Initializer extends BaseForgeInitializer {
        @Override
        public String getNamespace() {
            return Charm.MOD_ID;
        }
    }
}
