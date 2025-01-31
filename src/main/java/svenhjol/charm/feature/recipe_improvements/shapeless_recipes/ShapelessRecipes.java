package svenhjol.charm.feature.recipe_improvements.shapeless_recipes;

import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;
import svenhjol.charm.feature.recipe_improvements.shapeless_recipes.common.Providers;

@Feature(description = "Adds convenient shapeless recipe versions of common shaped vanilla recipes.")
public final class ShapelessRecipes extends CommonFeature implements ChildFeature<RecipeImprovements> {
    public final Providers providers;

    @Configurable(name = "Shapeless bread", description = "If true, adds a shapeless recipe for bread.")
    public static boolean bread = true;

    @Configurable(name = "Shapeless paper", description = "If true, adds a shapeless recipe for paper.")
    public static boolean paper = true;

    public ShapelessRecipes(CommonLoader loader) {
        super(loader);

        providers = new Providers(this);
    }

    @Override
    public Class<RecipeImprovements> typeForParent() {
        return RecipeImprovements.class;
    }
}
