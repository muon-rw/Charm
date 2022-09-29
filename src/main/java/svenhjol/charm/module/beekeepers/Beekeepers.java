package svenhjol.charm.module.beekeepers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Beekeepers are villagers that trade beekeeping items. Their job site is the beehive.\n" +
    "Disabling will leave villagers of the profession in an unemployed state with decrepit data.")
public class Beekeepers extends CharmModule {
    // TODO: Register this with the correct namespace in Charm 5.
    public static ResourceLocation ID = new ResourceLocation("charm_beekeeper");
    public static VillagerProfession BEEKEEPER;
    public static ResourceLocation GIFT;

    @Override
    public void register() {
        GIFT = new ResourceLocation(Charm.MOD_ID, "gameplay/hero_of_the_village/beekeeper_gift");
    }

    @Override
    public void runWhenEnabled() {
        BEEKEEPER = CommonRegistry.villagerProfession(ID, PoiType.BEEHIVE, List.of(), List.of(), SoundEvents.BEEHIVE_WORK);
        GiveGiftToHero.GIFTS.put(BEEKEEPER, GIFT);

        // HACK: set ticketCount so that villager can use it as job site
        PoiType.BEEHIVE.maxTickets = 1;

        // register beekeeper trades
        VillagerHelper.addTrade(BEEKEEPER, 1, new BeekeeperTradeOffers.EmeraldsForFlowers());
        VillagerHelper.addTrade(BEEKEEPER, 1, new BeekeeperTradeOffers.BottlesForEmerald());
        VillagerHelper.addTrade(BEEKEEPER, 2, new BeekeeperTradeOffers.EmeraldsForCharcoal());
        VillagerHelper.addTrade(BEEKEEPER, 2, new BeekeeperTradeOffers.CandlesForEmeralds());
        VillagerHelper.addTrade(BEEKEEPER, 3, new BeekeeperTradeOffers.EmeraldsForHoneycomb());
        VillagerHelper.addTrade(BEEKEEPER, 3, new BeekeeperTradeOffers.CampfireForEmerald());
        VillagerHelper.addTrade(BEEKEEPER, 4, new BeekeeperTradeOffers.LeadForEmeralds());
        VillagerHelper.addTrade(BEEKEEPER, 5, new BeekeeperTradeOffers.PopulatedBeehiveForEmeralds());
    }
}
