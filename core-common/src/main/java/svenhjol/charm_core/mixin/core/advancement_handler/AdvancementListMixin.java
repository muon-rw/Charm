package svenhjol.charm_core.mixin.core.advancement_handler;

import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm_core.init.AdvancementHandler;

import java.util.HashMap;
import java.util.Map;

@Mixin(AdvancementList.class)
public class AdvancementListMixin {
    /**
     * Conditionally remove advancements from the map if their
     * corresponding Charm module is disabled.
     * Disabling this mixin will cause all advancements to be
     * loaded and if any Charm modules are disabled then
     * some advancements may not be completable.
     */
    @Redirect(
        method = "add",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/Maps;newHashMap(Ljava/util/Map;)Ljava/util/HashMap;",
            remap = false
        )
    )
    private HashMap<ResourceLocation, Advancement.Builder> hookAdd(Map<ResourceLocation, Advancement.Builder> map) {
        var newMap = new HashMap<>(map);
        AdvancementHandler.filter(newMap);
        return Maps.newHashMap(newMap);
    }
}
