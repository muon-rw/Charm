package svenhjol.charm.feature.woodcutting;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, switchable = false, priority = 10, description = "Registers the woodcutting recipe.")
public class Woodcutting extends CharmFeature {
    private static final String RECIPE_ID = "woodcutting";
    public static Supplier<RecipeType<WoodcuttingRecipe>> RECIPE_TYPE;
    public static Supplier<RecipeSerializer<WoodcuttingRecipe>> RECIPE_SERIALIZER;

    @Override
    public void register() {
        RECIPE_TYPE = Charm.REGISTRY.recipeType(RECIPE_ID);
        RECIPE_SERIALIZER = Charm.REGISTRY.recipeSerializer(RECIPE_ID,
            () -> new WoodcuttingRecipe.Serializer<>(WoodcuttingRecipe::new));
    }
}
