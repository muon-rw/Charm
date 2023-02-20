package svenhjol.charm_core.forge.registry;

import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public record DeferredRecipeCategory<R extends Recipe<?>>(
    String id,
    Supplier<RecipeType<R>> recipeType,
    Supplier<RecipeBookType> recipeBookType
) { }
