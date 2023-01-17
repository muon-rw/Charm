package svenhjol.charm.feature.no_experimental_screen;


import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature(
    mod = Charm.MOD_ID,
    switchable = true,
    description = "Prevents the 'Experimental World' screen from showing with customized worldgen or tags present."
)
public class NoExperimentalScreen extends CharmFeature {

}
