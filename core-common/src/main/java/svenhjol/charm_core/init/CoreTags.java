package svenhjol.charm_core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import svenhjol.charm_core.CharmCore;

public class CoreTags {
    public static TagKey<Item> CHESTS = TagKey.create(BuiltInRegistries.ITEM.key(),
        CharmCore.makeId("chests/normal"));
}
