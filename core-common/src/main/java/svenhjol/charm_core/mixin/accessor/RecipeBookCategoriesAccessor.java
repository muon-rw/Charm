package svenhjol.charm_core.mixin.accessor;

import net.minecraft.client.RecipeBookCategories;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(RecipeBookCategories.class)
public interface RecipeBookCategoriesAccessor {
    /**
     * Used by Fabric to get the categories map and make it mutable.
     * @return aggregate categories map.
     */
    @Accessor("AGGREGATE_CATEGORIES")
    static Map<RecipeBookCategories, List<RecipeBookCategories>> getAggregateCategories() {
        throw new AssertionError();
    }

    /**
     * Used by Fabric to set the mutable categories map.
     * @param aggregateCategories Mutable aggregate categories map.
     */
    @Mutable
    @Accessor("AGGREGATE_CATEGORIES")
    static void setAggregateCategories(Map<RecipeBookCategories, List<RecipeBookCategories>> aggregateCategories) {
        throw new AssertionError();
    }
}
