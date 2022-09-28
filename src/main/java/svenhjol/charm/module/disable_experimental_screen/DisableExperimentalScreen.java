package svenhjol.charm.module.disable_experimental_screen;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Prevents the 'Experimental World' screen from showing when entering a world with Strange present.")
public class DisableExperimentalScreen extends CharmModule {
}
