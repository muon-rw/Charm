package svenhjol.charm.feature.kilns;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature
public class KilnsClient extends CharmFeature {
    @Override
    public void preRegister() {
        CharmClient.REGISTRY.recipeBookCategoryEnum("kiln_search", () -> Items.COMPASS);
        CharmClient.REGISTRY.recipeBookCategoryEnum("kiln", Kilns.BLOCK);
    }

    @Override
    public void register() {
        CharmClient.REGISTRY.menuScreen(Kilns.MENU, () -> KilnScreen::new);
        CharmClient.REGISTRY.recipeBookCategory("kiln", Firing.RECIPE_TYPE, Kilns.RECIPE_BOOK_TYPE);

        var enabled = Charm.LOADER.isEnabled(Kilns.class);
        addDependencyCheck(m -> enabled);

        if (enabled) {
            CharmClient.REGISTRY.itemTab(Kilns.BLOCK_ITEM, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.SMOKER);
        }
    }
}
