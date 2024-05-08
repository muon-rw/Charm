package svenhjol.charm.mixin;

import svenhjol.charm.Charm;
import svenhjol.charm.feature.extractable_enchantments.ExtractableEnchantments;
import svenhjol.charmony.base.CharmonyMixinConfigPlugin;
import svenhjol.charmony.helper.ConfigHelper;

import java.util.List;
import java.util.function.Predicate;

public class MixinConfigPlugin extends CharmonyMixinConfigPlugin {
    @Override
    protected String modId() {
        return Charm.ID;
    }

    @Override
    protected List<Predicate<String>> runtimeBlacklist() {
        return List.of(
            feature -> feature.equals("ColoredGlintSmithingTemplates") && ConfigHelper.isModLoaded("optifabric"),
            feature -> feature.equals("NoChatUnverifiedMessage") && ConfigHelper.isModLoaded("chatsigninghider"),
            feature -> feature.equals("ExtraStackables") && ConfigHelper.isModLoaded("allstackable"),
            feature -> feature.equals("Lumberjacks")
                    || feature.equals("Woodcutters")
                    || feature.equals("Woodcutting")
                    && ConfigHelper.isModLoaded("sawmill"),
            feature -> feature.equals("UnlimitedRepairCost")
                    || feature.equals("ShowRepairCost")
                    || feature.equals("StrongerAnvils")
                    || feature.equals("AerialAffinity")
                    || feature.equals("AerialAffinityEnchantment")
                    && ConfigHelper.isModLoaded("zenith"),
            feature -> feature.equals("AutoRestock")
                    || feature.equals("InventoryTidying")
                    && ConfigHelper.isModLoaded("inventoryprofilesnext"),
            feature -> feature.equals("NoExperimentalScreen")
                    && ConfigHelper.isModLoaded("yeetusexperimentus")
        );
    }
}
