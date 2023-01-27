package svenhjol.charm.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import svenhjol.charm.Charm;

public class CharmTags {
    public static TagKey<Item> CHESTS = TagKey.create(BuiltInRegistries.ITEM.key(), Charm.makeId("chests/normal"));
}
