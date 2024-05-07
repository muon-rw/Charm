package svenhjol.charm.feature.advancements.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.feature.Handler;
import svenhjol.charm.foundation.helper.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

public final class Handlers extends Handler<Advancements> {
    public List<String> fuzzyRemove = new ArrayList<>();
    public List<String> exactRemove = new ArrayList<>();

    public void packReload(String reason) {
        log().debug("Reloading Charm custom advancement filtering: " + reason);

        exactRemove.clear();
        fuzzyRemove.clear();

        for (var condition : feature().registers.get().conditions) {
            if (condition.test()) continue;
            condition.advancements().forEach(remove -> {
                if (remove.contains("*") || !remove.contains(":")) {
                    fuzzyRemove.add(remove);
                } else {
                    exactRemove.add(remove);
                }
            });
        }
    }

    public boolean shouldRemove(ResourceLocation id) {
        return ResourceLocationHelper.isDisabledCharmonyFeature(id)
            || ResourceLocationHelper.match(id, exactRemove, fuzzyRemove);
    }

    /**
     * Call by any mod to trigger the ActionPerformed advancement.
     */
    @SuppressWarnings("unused")
    public void trigger(ResourceLocation advancement, Player player) {
        // Don't do anything on the client.
        if (!player.level().isClientSide) {
            feature().registers.get().actionPerformed.trigger(advancement, (ServerPlayer)player);
        }
    }

    @Override
    protected Class<Advancements> type() {
        return Advancements.class;
    }
}
