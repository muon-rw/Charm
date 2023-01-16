package svenhjol.charm.feature.no_potion_glint;

import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature(
    mod = Charm.MOD_ID,
    switchable = true,
    description = "Removes the potion enchantment glint so you can see what the potion color is."
)
public class NoPotionGlint extends CharmFeature { }
