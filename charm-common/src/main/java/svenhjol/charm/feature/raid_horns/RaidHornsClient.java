package svenhjol.charm.feature.raid_horns;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@SuppressWarnings("deprecation")
@ClientFeature
public class RaidHornsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(RaidHorns.class));
    }

    @Override
    public void register() {
        CharmClient.REGISTRY.itemProperties("minecraft:tooting",
            RaidHorns.ITEM, () -> this::handleTooting);

        if (isEnabled()) {
            CharmClient.REGISTRY.itemTab(
                RaidHorns.ITEM,
                CreativeModeTabs.TOOLS_AND_UTILITIES,
                Items.TNT_MINECART
            );
        }
    }

    private float handleTooting(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        return entity != null
            && entity.isUsingItem()
            && entity.getUseItem() == stack ? 1.0F : 0.0F;
    }
}
