package svenhjol.charm_core.init;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm_core.CharmCore;
import svenhjol.charm_api.iface.IRemovesAdvancements;
import svenhjol.charm_core.base.CharmFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AdvancementHandler {

    /**
     * Holds advancements that should be filtered when filterAdvancements is run.
     */
    private static final List<ResourceLocation> REMOVE = new ArrayList<>();

    public static void filter(Map<ResourceLocation, Advancement.Builder> map) {
        List<ResourceLocation> toRemove = new ArrayList<>(REMOVE);

        toRemove.addAll(CharmApi.getProviderData(IRemovesAdvancements.class,
            provider -> provider.getAdvancementsToRemove().stream()));

        toRemove.forEach(removeable -> {
            List<ResourceLocation> keys = new ArrayList<>(map.keySet());

            // Remove exact matches.
            AtomicInteger exactMatches = new AtomicInteger();
            keys.stream().filter(a -> a.equals(removeable)).forEach(a -> {
                CharmCore.LOG.debug(AdvancementHandler.class, "Filtering out exact match `" + a + "`");
                exactMatches.getAndIncrement();
                map.remove(a);
            });
            if (exactMatches.intValue() > 0)
                return;

            // Remove all advancements for disabled modules.
            keys.stream()
                .filter(a -> a.getNamespace().equals(removeable.getNamespace()))
                .filter(a -> a.getPath().startsWith(removeable.getPath()))
                .forEach(a -> {
                    CharmCore.LOG.debug(AdvancementHandler.class, "Filtering out fuzzy match `" + a + "`");
                    map.remove(a);
                });
        });
    }

    public static void remove(CharmFeature feature) {
        var featureId = feature.getFeatureId();

        if (!REMOVE.contains(featureId)) {
            REMOVE.add(featureId);
        }
    }

    public static void trigger(ResourceLocation advancement, ServerPlayer player) {
        // TODO: Trigger the advancement.
    }
}
