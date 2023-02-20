package svenhjol.charm_api.iface;

import net.minecraft.world.level.ItemLike;

public interface IWandererTrade {
    ItemLike getItem();

    int getCount();

    int getCost();
}
