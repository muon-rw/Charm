package svenhjol.charm_core.base.item;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import svenhjol.charm_core.base.CharmFeature;

public class CharmBoatItem extends BoatItem {
    protected final CharmFeature feature;

    public CharmBoatItem(CharmFeature feature, boolean isChestBoat, Boat.Type type) {
        super(isChestBoat, type, new Properties().stacksTo(1));
        this.feature = feature;
    }
}
