package svenhjol.charm.feature.beekeepers;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.helper.GenericTradeOffers;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Beekeepers are villagers that trade beekeeping items. Their job site is the beehive.")
public class Beekeepers extends CharmFeature {
    private static final String VILLAGER_ID = "beekeeper";
    private static final String BLOCK_ID = "minecraft:beehive";
    public static Supplier<VillagerProfession> VILLAGER_PROFESSION;

    @Override
    public void register() {
        VILLAGER_PROFESSION = Charm.REGISTRY.villagerProfession(
            VILLAGER_ID, BLOCK_ID, List.of(), () -> SoundEvents.BEEHIVE_WORK);

        Charm.REGISTRY.villagerGift(VILLAGER_ID);

        if (isEnabled()) {
            addTrades();
        }
    }

    private void addTrades() {
        // Tier 1

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 1, () -> new BeekeeperTradeOffers.EmeraldsForFlowers(
            3, 13, 1, 0, 2, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 1, () -> new GenericTradeOffers.ItemsForEmeralds(
            Items.GLASS_BOTTLE, 2, 4, 1, 0, 2, 20));

        // Tier 2

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 2, () -> new GenericTradeOffers.EmeraldsForItems(
            Items.CHARCOAL, 13, 3, 1, 0, 5, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            ItemTags.CANDLES, 3, 0, 1, 0, 5, 20));

        // Tier 3

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 3, () -> new GenericTradeOffers.EmeraldsForItems(
            Items.HONEYCOMB, 10, 0, 1, 2, 10, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 3, () -> new GenericTradeOffers.ItemsForEmeralds(
            Items.CAMPFIRE, 1, 0, 1, 0, 10, 20));

        // Tier 4

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Items.LEAD, 6, 0, 1, 0, 15, 10));

        // Tier 5

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 5, () -> new BeekeeperTradeOffers.PopulatedBeehiveForEmeralds(
            21, 14, 30, 1));
    }
}
