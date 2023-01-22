package svenhjol.charm.feature.firing;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, switchable = false, priority = 10, description = "Registers the firing recipe.")
public class Firing extends CharmFeature {
    private static final String RECIPE_ID = "firing";
    public static Supplier<RecipeType<FiringRecipe>> RECIPE_TYPE;
    public static Supplier<SimpleCookingSerializer<FiringRecipe>> RECIPE_SERIALIZER;

    @Override
    public void register() {
        RECIPE_TYPE = Charm.REGISTRY.recipeType(RECIPE_ID);
        RECIPE_SERIALIZER = Charm.REGISTRY.recipeSerializer(RECIPE_ID,
            () -> new SimpleCookingSerializer<>(FiringRecipe::new, 100));
    }
}
