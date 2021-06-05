package svenhjol.charm.init;

import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.LootHelper;

public class CharmLoot {
    public static Identifier VILLAGE_LUMBERJACK = new Identifier(Charm.MOD_ID, "chests/village/village_lumberjack");
    public static Identifier VILLAGE_BEEKEEPER = new Identifier(Charm.MOD_ID, "chests/village/village_beekeeper");
    public static Identifier VILLAGE_LIBRARIAN = new Identifier(Charm.MOD_ID, "chests/village/village_librarian");

    public static void init() {
        LootHelper.CUSTOM_LOOT_TABLES.add(VILLAGE_LUMBERJACK);
        LootHelper.CUSTOM_LOOT_TABLES.add(VILLAGE_BEEKEEPER);
        LootHelper.CUSTOM_LOOT_TABLES.add(VILLAGE_LIBRARIAN);
    }
}
