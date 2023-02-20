package svenhjol.charm_api.iface;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public interface IGrindsItems {
    List<Pair<ItemLike, ItemLike>> getItemGrindResults();
}
