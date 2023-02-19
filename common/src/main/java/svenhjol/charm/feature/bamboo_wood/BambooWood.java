package svenhjol.charm.feature.bamboo_wood;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_barrels.VariantBarrels;
import svenhjol.charm.feature.variant_chests.VariantChests;
import svenhjol.charm.feature.variant_ladders.VariantLadders;
import svenhjol.charm.feature.wood.Wood;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charm_api.CharmApi;
import svenhjol.charm_api.iface.IRemovesRecipes;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.ArrayList;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Bamboo barrels, bookcases, chests and ladders.")
public class BambooWood extends CharmFeature implements IRemovesRecipes {
    @Override
    public void register() {
        var material = BambooMaterial.BAMBOO;
        var registry = Charm.REGISTRY;

        Wood.registerBarrel(registry, material);
        Wood.registerBookshelf(registry, material);
        Wood.registerChiseledBookshelf(registry, material);
        Wood.registerChest(registry, material);
        Wood.registerTrappedChest(registry, material);
        Wood.registerLadder(registry, material);

        CharmApi.registerProvider(this);
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        List<ResourceLocation> remove = new ArrayList<>();

        if (!Charm.LOADER.isEnabled(VariantBarrels.class)) {
            remove.add(Charm.makeId("bamboo_barrel"));
        }

        if (!Charm.LOADER.isEnabled(VariantChests.class)) {
            remove.add(Charm.makeId("bamboo_chest"));
            remove.add(Charm.makeId("bamboo_trapped_chest"));
        }

        if (!Charm.LOADER.isEnabled(VariantLadders.class)) {
            remove.add(Charm.makeId("bamboo_ladder"));
        }

        if (!Charm.LOADER.isEnabled(Woodcutters.class)) {
            remove.add(Charm.makeId("bamboo_wood/woodcutting/"));
        }

        return remove;
    }
}
