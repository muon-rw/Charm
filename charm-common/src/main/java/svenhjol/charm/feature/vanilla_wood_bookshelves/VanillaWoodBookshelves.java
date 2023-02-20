package svenhjol.charm.feature.vanilla_wood_bookshelves;

import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_bookshelves.VariantBookshelves;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.enums.VanillaWood;

@Feature(mod = Charm.MOD_ID, description = "Bookshelves in all vanilla wood types.")
public class VanillaWoodBookshelves extends CharmFeature {
    @Override
    public void register() {
        for (var material  : VanillaWood.getTypes()) {
            VariantBookshelves.registerBookshelf(Charm.REGISTRY, material);
        }
    }
}
