package svenhjol.charm.module;

import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.ColoredBundlesClient;
import svenhjol.charm.item.ColoredBundleItem;
import svenhjol.charm.recipe.BundleColoringRecipe;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, client = ColoredBundlesClient.class, description = "Allows bundles to be dyed.")
public class ColoredBundles extends CharmModule {
    public static final Identifier RECIPE_ID = new Identifier(Charm.MOD_ID, "crafting_special_bundlecoloring");
    public static final Map<DyeColor, ColoredBundleItem> COLORED_BUNDLES = new HashMap<>();
    public static SpecialRecipeSerializer<BundleColoringRecipe> BUNDLE_COLORING_RECIPE;

    @Override
    public void register() {
        for (DyeColor color : DyeColor.values()) {
            COLORED_BUNDLES.put(color, new ColoredBundleItem(this, color));
        }

        BUNDLE_COLORING_RECIPE = RegistryHandler.recipeSerializer(RECIPE_ID.toString(), new SpecialRecipeSerializer<>(BundleColoringRecipe::new));
    }
}
