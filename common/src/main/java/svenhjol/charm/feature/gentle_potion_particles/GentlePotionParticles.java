package svenhjol.charm.feature.gentle_potion_particles;

import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature(
    mod = Charm.MOD_ID,
    switchable = true,
    description = "Potion effect particles are less obtrusive."
)
public class GentlePotionParticles extends CharmFeature { }
