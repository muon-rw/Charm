package svenhjol.charm.feature.beekeepers;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.beekeepers.common.Advancements;
import svenhjol.charm.feature.beekeepers.common.Registers;
import svenhjol.charm.feature.beekeepers.common.Trades;

@Feature(description = "Beekeepers are villagers that trade beekeeping items. Their job site is the beehive.")
public final class Beekeepers extends CommonFeature {
    public static final String VILLAGER_ID = "beekeeper";
    public static final String BLOCK_ID = "minecraft:beehive";

    public final Registers registers;
    public final Advancements advancements;
    public final Trades trades;

    public Beekeepers(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
        trades = new Trades(this);
    }
}
