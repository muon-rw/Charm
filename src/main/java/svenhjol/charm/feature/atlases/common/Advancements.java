package svenhjol.charm.feature.atlases.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<Atlases> {
    public static final int NUMBER_OF_MAPS_FOR_ACHIEVEMENT = 10;

    public Advancements(Atlases feature) {
        super(feature);
    }

    public void madeAtlasMaps(Player player) {
        trigger("made_atlas_maps", player);
    }
}
