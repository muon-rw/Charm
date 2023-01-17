package svenhjol.charm.fabric;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.Charm;
import svenhjol.charm_core.fabric.base.BaseFabricInitializer;

public class FabricModInitializer implements ModInitializer {
    private Charm mod;

    public static final Initializer INIT = new Initializer();

    @Override
    public void onInitialize() {
        // Always init Core first.
        svenhjol.charm_core.fabric.FabricModInitializer.initCharmCore();

        if (mod == null) {
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
