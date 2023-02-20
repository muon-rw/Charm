package svenhjol.charm_core.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm_core.CharmCore;
import svenhjol.charm_core.forge.base.BaseForgeInitializer;
import svenhjol.charm_core.forge.recipe.SortingRecipeManager;

@Mod(CharmCore.MOD_ID)
public class ForgeModInitializer {
    private final CharmCore mod;
    public static final Initializer INIT = new Initializer();

    public ForgeModInitializer() {
        var modEventBus = INIT.getModEventBus();
        modEventBus.addListener(this::handleCommonSetup);

        // Charm Core adds a recipe sorting manager.
        MinecraftForge.EVENT_BUS.addListener(this::handleAddReloadListener);

        mod = new CharmCore(INIT);

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

    private void handleAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new SortingRecipeManager());
    }

    public static class Initializer extends BaseForgeInitializer {
        @Override
        public String getNamespace() {
            return CharmCore.MOD_ID;
        }
    }
}
