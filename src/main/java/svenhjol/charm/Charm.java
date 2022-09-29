package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.init.CharmDebug;
import svenhjol.charm.init.CharmHacks;
import svenhjol.charm.init.CharmParticles;
import svenhjol.charm.init.CharmResources;
import svenhjol.charm.lib.CharmAdvancements;
import svenhjol.charm.lib.CharmTags;
import svenhjol.charm.lib.LogWrapper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.loader.CommonLoader;

@SuppressWarnings("unused")
public class Charm implements ModInitializer {
    public static final String MOD_ID = "charm";
    public static final LogWrapper LOG = new LogWrapper(MOD_ID);
    public static final CommonLoader<CharmModule> LOADER = new CommonLoader<>(MOD_ID, "svenhjol.charm.module");

    private static boolean hasStartedCharm = false;

    @Override
    public void onInitialize() {
        init();
    }

    public static void init() {
        if (hasStartedCharm) return;

        CharmDebug.init();
        CharmHacks.init();
        CharmResources.init();
        CharmParticles.init();
        CharmTags.init();
        CharmAdvancements.init();

        LOADER.init();

        hasStartedCharm = true;
    }
}
