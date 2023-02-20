package svenhjol.charm_core.fabric.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import svenhjol.charm_core.CharmCore;
import svenhjol.charm_core.init.RecipeHandler;
import svenhjol.charm_core.mixin.accessor.RecipeManagerAccessor;

import java.util.Map;

public class SortingRecipeManager extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();

    public SortingRecipeManager() {
        super(GSON, "charm_sorting_recipe_manager");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        if (RecipeHandler.RECIPE_MANAGER_HOLDER != null) {
            CharmCore.LOG.debug(getClass(), RecipeHandler.RECIPE_MANAGER_HOLDER.toString());
            var recipes = ((RecipeManagerAccessor)RecipeHandler.RECIPE_MANAGER_HOLDER).getRecipes();

            if (!recipes.isEmpty()) {
                ((RecipeManagerAccessor)RecipeHandler.RECIPE_MANAGER_HOLDER).setRecipes(RecipeHandler.sortAndFilter(recipes, true));
            }
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return CharmCore.makeId("sorting_recipe_manager");
    }
}
