package svenhjol.charm_core.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm_core.CharmCore;
import svenhjol.charm_api.iface.IRemovesRecipes;
import svenhjol.charm_core.helper.TextHelper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipeHandler {
    /**
     * Holds a reference to the global RecipeManager.
     * @see svenhjol.charm_core.mixin.core.recipe_manager_holder.ReloadableServerResourcesMixin
     */
    public static RecipeManager RECIPE_MANAGER_HOLDER;

    public static Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> sortAndFilter(Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> byType, boolean doFilter) {
        CharmCore.LOG.debug(RecipeHandler.class, "Preparing to sort and filter recipes.");
        Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> out = new LinkedHashMap<>();

        var toRemove = CharmApi.getProviderData(IRemovesRecipes.class,
            provider -> provider.getRecipesToRemove().stream());

        for (var type : byType.keySet()) {
            var recipes = byType.get(type);
            CharmCore.LOG.debug(RecipeHandler.class, "Recipe type " + type.toString() + " contains " + recipes.size() + " recipes.");

            var modded = recipes.entrySet().stream().filter(r -> !r.getKey().getNamespace().equals("minecraft"));
            var vanilla = recipes.entrySet().stream().filter(r -> r.getKey().getNamespace().equals("minecraft"));

            if (doFilter) {
                modded = modded.filter(r -> {
                    var key = r.getKey();
                    var namespace = key.getNamespace();
                    var path = key.getPath();

                    // If the recipe is not a charm-based mod, let it pass.
                    if (!GlobalLoaders.LOADERS.containsKey(namespace)) {
                        return true;
                    }

                    var featureName = TextHelper.snakeToUpperCamel(path.split("/")[0]);

                    // Remove recipes for disabled charm features and recipes.
                    if (!GlobalLoaders.LOADERS.get(namespace).isEnabled(featureName)) {
                        CharmCore.LOG.debug(RecipeHandler.class, "Feature " + featureName + " not enabled, removing " + key);
                        return false;
                    }

                    // Remove recipe if it matches the exact key.
                    if (toRemove.contains(key)) {
                        CharmCore.LOG.debug(RecipeHandler.class, "API flagged to remove, removing " + key);
                        return false;
                    }

                    // Remove recipe if the path ends with a /.
                    // Can't use wildcard symbol here because it's not a valid resourcelocation character.
                    if (toRemove.stream().anyMatch(re -> re.getPath().endsWith("/") && key.getPath().startsWith(re.getPath()))) {
                        CharmCore.LOG.debug(RecipeHandler.class, "API fuzzy match, removing " + key);
                        return false;
                    }

                    return true;
                });
            }


            // Rebuild the recipes for this recipe type.
            Map<ResourceLocation, Recipe<?>> merged = new LinkedHashMap<>();
            var moddedRecipes = modded.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            var vanillaRecipes = vanilla.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            merged.putAll(moddedRecipes);
            merged.putAll(vanillaRecipes);

            out.put(type, merged);

            CharmCore.LOG.debug(RecipeHandler.class, "Recipe type " + type + " reassembled with " + merged.size() + " recipes");
        }

        return out;
    }
}
