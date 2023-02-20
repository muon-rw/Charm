package svenhjol.charm.fabric;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.Charm;
import svenhjol.charm_core.fabric.base.BaseFabricInitializer;

public class FabricModInitializer implements ModInitializer {
    private static Charm mod;
    public static final Initializer INIT = new Initializer();

    @Override
    public void onInitialize() {
        initCharm();
    }

    public static void initCharm() {
        if (mod == null) {
            // Always init Core first.
            svenhjol.charm_core.fabric.FabricModInitializer.initCharmCore();

            mod = new Charm(INIT);
            mod.run();
        }
    }

    public static class Initializer extends BaseFabricInitializer {
        @Override
        public String getNamespace() {
            return Charm.MOD_ID;
        }
    }
}
