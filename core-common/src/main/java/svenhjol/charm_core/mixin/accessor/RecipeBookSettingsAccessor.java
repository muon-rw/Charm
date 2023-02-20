package svenhjol.charm_core.mixin.accessor;

import com.mojang.datafixers.util.Pair;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RecipeBookSettings.class)
public interface RecipeBookSettingsAccessor {
    @Mutable
    @Accessor("TAG_FIELDS")
    static Map<RecipeBookType, Pair<String, String>> getTagFields() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("TAG_FIELDS")
    static void setTagFields(Map<RecipeBookType, Pair<String, String>> tagFields) {
        throw new AssertionError();
    }
}
