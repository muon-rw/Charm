package svenhjol.charm.feature.lumberjacks;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.helper.GenericTradeOffers;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.")
public class Lumberjacks extends CharmFeature {
    private static final String VILLAGER_ID = "lumberjack";
    public static Supplier<VillagerProfession> VILLAGER_PROFESSION;
    public static Supplier<SoundEvent> WORK_SOUND;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(Woodcutters.class));
    }

    @Override
    public void register() {
        WORK_SOUND = Charm.REGISTRY.soundEvent("lumberjack");

        VILLAGER_PROFESSION = Charm.REGISTRY.villagerProfession(VILLAGER_ID, Woodcutters.BLOCK_ID, List.of(), WORK_SOUND);

        Charm.REGISTRY.villagerGift(VILLAGER_ID);

        if (isEnabled()) {
            addTrades();
        }
    }

    private void addTrades() {
        // Tier 1

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 1, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.LOGS_THAT_BURN, 8, 1, 2, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 1, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.SAPLINGS, 1, 1, 2, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 1, () -> new GenericTradeOffers.EmeraldsForItems(
            Items.LADDER, 1, 1, 2, 20));

        // Tier 2

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 2, () -> new GenericTradeOffers.EmeraldsForItems(
            Items.BONE, 10, 2, 1, 0, 5, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.BEDS, 3, 1, 7, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.WOODEN_FENCES, 1, 2, 6, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 2, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.FENCE_GATES, 1, 2, 6, 20));

        // Tier 3

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 3, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.WARPED_STEMS, 7, 1, 10, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 3, () -> new GenericTradeOffers.EmeraldsForTag<>(
            BlockTags.CRIMSON_STEMS, 7, 1, 10, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 3, () -> new LumberjackTradeOffers.BarkForLogs(
            5, 12, 1, 0, 10, 10));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 3, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.WOODEN_DOORS, 1, 1, 10, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 3, () -> new GenericTradeOffers.TagForEmeralds<>(
            BlockTags.WOODEN_TRAPDOORS, 1, 1, 10, 20));

        // Tier 4

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.BARREL, 3, 1, 1, 1, 15, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.CHEST, 3, 1, 1, 1, 15, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.NOTE_BLOCK, 8, 1, 15, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 4, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.JUKEBOX, 8, 1, 15, 20));

        // Tier 5

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.FLETCHING_TABLE, 5, 1, 30, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.CRAFTING_TABLE, 5, 1, 30, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.CARTOGRAPHY_TABLE, 5, 1, 30, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.LOOM, 5, 1, 30, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Blocks.COMPOSTER, 5, 1, 30, 20));

        Charm.REGISTRY.villagerTrade(VILLAGER_PROFESSION, 5, () -> new GenericTradeOffers.ItemsForEmeralds(
            Woodcutters.BLOCK.get(), 5, 1, 30, 20));
    }
}
