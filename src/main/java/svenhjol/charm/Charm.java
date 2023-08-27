package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import svenhjol.charm_core.Log;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmConfig;
import svenhjol.charm_core.base.CharmLoader;
import svenhjol.charm_core.common.CommonConfig;
import svenhjol.charm_core.common.CommonEvents;
import svenhjol.charm_core.common.CommonLoader;
import svenhjol.charm_core.common.CommonRegistry;
import svenhjol.charm_core.iface.ILog;
import svenhjol.charm_core.server.ServerNetwork;

@Mod(Charm.MOD_ID)
public class Charm {
    public static final String MOD_ID = "charm";
    public static final String PREFIX = "svenhjol." + MOD_ID;
    public static final String FEATURE_PREFIX = PREFIX + ".feature";
    public static ILog LOG;
    public static CharmLoader LOADER;
    public static CommonRegistry REGISTRY;
    public static CommonEvents EVENTS;
    public static ServerNetwork NETWORK;
    public static CharmConfig CONFIG;

    public Charm() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::handleCommonSetup);

        LOG = new Log();
        CONFIG = new CommonConfig(LOG);
        REGISTRY = new CommonRegistry(MOD_ID, LOG);
        EVENTS = new CommonEvents(LOG, REGISTRY, modEventBus);
        NETWORK = new ServerNetwork(LOG);
        LOADER = new CommonLoader(MOD_ID, LOG, CONFIG);

        // Autoload all annotated features from the feature namespace.
        LOADER.init(FEATURE_PREFIX, Feature.class);

        // Listen to Forge config changes.
        modEventBus.addListener(CONFIG::refresh);

        // Execute client init so that client registration happens.
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CharmClient::new);
    }

    public static ResourceLocation makeId(String id) {
        return !id.contains(":") ? new ResourceLocation(MOD_ID, id) : new ResourceLocation(id);
    }

    private void handleCommonSetup(FMLCommonSetupEvent event) {
        LOADER.run();
    }
}
