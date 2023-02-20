package svenhjol.charm_core.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import svenhjol.charm_core.CharmCore;
import svenhjol.charm_core.fabric.base.BaseFabricInitializer;
import svenhjol.charm_core.fabric.recipe.SortingRecipeManager;

public class FabricModInitializer implements ModInitializer {
    private static CharmCore mod;
    public static final Initializer INIT = new Initializer();

    @Override
    public void onInitialize() {
        initCharmCore();
    }

    public static void initCharmCore() {
        if (mod == null) {
            mod = new CharmCore(INIT);

            // Add our custom SortingRecipeManager as a reloadable resource.
            ResourceManagerHelper.get(PackType.SERVER_DATA)
                .registerReloadListener(new SortingRecipeManager());

            mod.run();
        }
    }

    public static class Initializer extends BaseFabricInitializer {
        @Override
        public String getNamespace() {
            return CharmCore.MOD_ID;
        }
    }
}
