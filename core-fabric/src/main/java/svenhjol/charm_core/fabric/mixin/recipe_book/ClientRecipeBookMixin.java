package svenhjol.charm_core.fabric.mixin.recipe_book;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm_core.fabric.client.ClientRegistry;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(
        method = "getCategory",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetCategory(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir) {
        var getMainCategory = ClientRegistry.getRecipeBookMainCategory();
        RecipeType<?> recipeType = recipe.getType();

        if (getMainCategory.containsKey(recipeType)) {
            cir.setReturnValue(getMainCategory.get(recipeType));
        }
    }
}
