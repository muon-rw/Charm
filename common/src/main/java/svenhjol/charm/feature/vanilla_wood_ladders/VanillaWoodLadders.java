package svenhjol.charm.feature.vanilla_wood_ladders;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_ladders.VariantLadders;
import svenhjol.charm_api.iface.IRemovesRecipes;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.enums.VanillaWood;
import svenhjol.charm_core.init.CharmApi;
import svenhjol.charm_core.init.GlobalLoaders;

import java.util.ArrayList;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Ladders in all vanilla wood types.")
public class VanillaWoodLadders extends CharmFeature implements IRemovesRecipes {
    @Override
    public void register() {
        for (var material : VanillaWood.getTypes()) {
            VariantLadders.registerLadder(material);
        }

        CharmApi.registerProvider(this);
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        List<ResourceLocation> remove = new ArrayList<>();

        if (!GlobalLoaders.isEnabled(new ResourceLocation("charm_world", "woodcutters"))) {
            remove.add(Charm.makeId("vanilla_wood_ladders/woodcutting/"));
        }

        return remove;
    }
}
