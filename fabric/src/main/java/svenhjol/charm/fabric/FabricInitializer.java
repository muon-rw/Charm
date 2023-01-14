package svenhjol.charm.fabric;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.Charm;

public class FabricInitializer implements ModInitializer {
    private Charm mod;

    public static final Initializer INIT = new Initializer();

    @Override
    public void onInitialize() {
        if (mod == null) {
            mod = new Charm(INIT);
            mod.run();
        }
    }

    // TODO: do we need initCharmFirst() anymore now we have core?

    public static class Initializer extends svenhjol.charm_core.fabric.FabricInitializer.Initializer {
        @Override
        public String getNamespace() {
            return Charm.MOD_ID;
        }
    }
}
