package svenhjol.charm.module.lumberjacks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.lumberjacks.LumberjackTradeOffers.*;
import svenhjol.charm.module.woodcutters.Woodcutters;
import svenhjol.charm.registry.CommonRegistry;

import static svenhjol.charm.helper.VillagerHelper.addTrade;

@CommonModule(mod = Charm.MOD_ID, description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.\n" +
    "Disabling will leave villagers of the profession in an unemployed state with decrepit data.")
public class Lumberjacks extends CharmModule {
    public static String VILLAGER_ID = "charm_lumberjack";
    public static VillagerProfession LUMBERJACK;

    public static ResourceKey<PoiType> POIT;
    public static SoundEvent VILLAGER_WORK_LUMBERJACK;
    public static ResourceLocation GIFT;

    @Override
    public void register() {
        VILLAGER_WORK_LUMBERJACK = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "lumberjack"));
        POIT = CommonRegistry.pointOfInterestType(Woodcutters.BLOCK_ID, Woodcutters.WOODCUTTER, 1);
        GIFT = new ResourceLocation(Charm.MOD_ID, "gameplay/hero_of_the_village/lumberjack_gift");

        addDependencyCheck(m -> Charm.LOADER.isEnabled(Woodcutters.class));
    }

    @Override
    public void runWhenEnabled() {
        LUMBERJACK = VillagerHelper.addProfession(VILLAGER_ID, POIT, VILLAGER_WORK_LUMBERJACK);
        GiveGiftToHero.GIFTS.put(LUMBERJACK, GIFT);

        // register lumberjack trades
        addTrade(LUMBERJACK, 1, new EmeraldsForOverworldLogs());
        addTrade(LUMBERJACK, 1, new CommonSaplingsForEmeralds());
        addTrade(LUMBERJACK, 1, new LaddersForEmeralds());
        addTrade(LUMBERJACK, 2, new EmeraldsForBones());
        addTrade(LUMBERJACK, 2, new BedForEmeralds());
        addTrade(LUMBERJACK, 2, new FencesForEmeralds());
        addTrade(LUMBERJACK, 3, new BarkForLogs());
        addTrade(LUMBERJACK, 3, new EmeraldsForStems());
        addTrade(LUMBERJACK, 3, new DoorsForEmeralds());
        addTrade(LUMBERJACK, 3, new UncommonSaplingsForEmeralds());
        addTrade(LUMBERJACK, 4, new BarrelForEmeralds());
        addTrade(LUMBERJACK, 4, new MusicBlocksForLogs());
        addTrade(LUMBERJACK, 5, new WorkstationForEmeralds());
    }
}
