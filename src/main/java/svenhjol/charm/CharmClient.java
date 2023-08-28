package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import svenhjol.charm_core.Log;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmConfig;
import svenhjol.charm_core.base.CharmLoader;
import svenhjol.charm_core.client.*;
import svenhjol.charm_core.iface.*;

public class CharmClient {
    public static final String MOD_ID = Charm.MOD_ID;
    public static final String PREFIX = "svenhjol." + MOD_ID;
    public static final String FEATURE_PREFIX = PREFIX + ".feature";
    public static ILog LOG;
    public static CharmConfig CONFIG;
    public static CharmLoader LOADER;
    public static ClientEvents EVENTS;
    public static ClientRegistry REGISTRY;
    public static ClientNetwork NETWORK;

    public CharmClient() {
        // Get a reference to the mod event bus for adding listeners.
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::handleClientSetup);

        // Initialize services.
        LOG = new Log();
        CONFIG = new ClientConfig(LOG);
        LOADER = new ClientLoader(MOD_ID, LOG, CONFIG);
        REGISTRY = new ClientRegistry(MOD_ID, LOG);
        EVENTS = new ClientEvents(LOG, REGISTRY, modEventBus);
        NETWORK = new ClientNetwork(LOG);

        // Listen to Forge config changes.
        modEventBus.addListener(CONFIG::refresh);

        // Autoload all annotated client features from the feature namespace.
        LOADER.init(FEATURE_PREFIX, ClientFeature.class);
    }

    public static ResourceLocation makeId(String id) {
        return !id.contains(":") ? new ResourceLocation(MOD_ID, id) : new ResourceLocation(id);
    }

    private void handleClientSetup(FMLClientSetupEvent event) {
        LOADER.run();

        // Do final registry tasks.
        event.enqueueWork(EVENTS::doFinalTasks);
    }
}
