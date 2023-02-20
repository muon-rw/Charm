package svenhjol.charm_api.iface;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.BiPredicate;

public interface IChecksInventoryTag {
    List<BiPredicate<Player, TagKey<Item>>> getInventoryTagChecks();
}
