package svenhjol.charm.lib;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.Charm;
import svenhjol.charm.advancement.ActionPerformedCriterion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CharmAdvancements {
    private static final List<ResourceLocation> ADVANCEMENTS_TO_REMOVE = new ArrayList<>();
    private static ActionPerformedCriterion ACTION_PERFORMED;

    public static void init() {
        ACTION_PERFORMED = CriteriaTriggers.register(new ActionPerformedCriterion());
    }

    /**
     * Trigger the generic Charm ACTION_PERFORMED criteria.
     */
    public static void trigger(ServerPlayer player, ResourceLocation id) {
        ACTION_PERFORMED.trigger(player, id);
    }

    /**
     * Remove a specific advancement from the advancement tree.
     */
    public static void remove(ResourceLocation id) {
        Charm.LOG.debug(CharmAdvancements.class, "Adding `" + id + "` to list of advancements to remove");
        ADVANCEMENTS_TO_REMOVE.add(id);
    }

    /**
     * Conditionally remove advancements from the map if their corresponding Charm module is disabled.
     * This is called from {@link svenhjol.charm.mixin.helper.FilterAdvancementsMixin}.
     */
    public static void filter(Map<ResourceLocation, Advancement.Builder> map) {
        if (ADVANCEMENTS_TO_REMOVE.isEmpty() || map.isEmpty()) return;

        for (ResourceLocation id : ADVANCEMENTS_TO_REMOVE) {
            List<ResourceLocation> keys = new ArrayList<>(map.keySet());

            // Remove exact matches.
            var exactMatches = new AtomicInteger();
            keys.stream()
                .filter(a -> a.equals(id))
                .forEach(a -> {
                    Charm.LOG.debug(CharmAdvancements.class, "> Filtering out exact match `" + a + "`");
                    exactMatches.getAndIncrement();
                    map.remove(a);
                });

            if (exactMatches.intValue() > 0) {
                return;
            }

            // Remove all advancements for disabled modules.
            keys.stream()
                .filter(a -> a.getNamespace().equals(id.getNamespace()))
                .filter(a -> a.getPath().startsWith(id.getPath()))
                .forEach(a -> {
                    Charm.LOG.debug(CharmAdvancements.class, "> Filtering out fuzzy match `" + a + "`");
                    map.remove(a);
                });
        }
    }
}
