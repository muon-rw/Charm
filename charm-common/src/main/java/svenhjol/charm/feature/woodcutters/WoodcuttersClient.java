package svenhjol.charm.feature.woodcutters;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class WoodcuttersClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(Woodcutters.class));
    }

    @Override
    public void preRegister() {
        CharmClient.REGISTRY.recipeBookCategoryEnum("woodcutter_search", () -> Items.COMPASS);
        CharmClient.REGISTRY.recipeBookCategoryEnum("woodcutter", Woodcutters.BLOCK);
    }

    @Override
    public void register() {
        CharmClient.REGISTRY.menuScreen(Woodcutters.MENU, () -> WoodcutterScreen::new);
        CharmClient.REGISTRY.recipeBookCategory("woodcutter", Woodcutting.RECIPE_TYPE, Woodcutters.RECIPE_BOOK_TYPE);

        // The woodcutter block should render transparent areas cut out.
        CharmClient.REGISTRY.blockRenderType(Woodcutters.BLOCK, RenderType::cutout);

        if (isEnabled()) {
            CharmClient.REGISTRY.itemTab(Woodcutters.BLOCK, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.STONECUTTER);
        }
    }
}