package svenhjol.charm.feature.variant_ladders;

import svenhjol.charm.Charm;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.iface.IRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, switchable = false, description = "Registers variant ladders.")
public class VariantLadders extends CharmFeature {
    public static final Map<IVariantMaterial, Supplier<VariantLadderBlock>> LADDER_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, Supplier<VariantLadderBlock.BlockItem>> LADDER_BLOCK_ITEMS = new HashMap<>();

    public static void registerLadder(IRegistry registry, IVariantMaterial material) {
        var id = material.getSerializedName() + "_ladder";

        var block = registry.block(id, () -> new VariantLadderBlock(material));
        var blockItem = registry.item(id, () -> new VariantLadderBlock.BlockItem(block));

        LADDER_BLOCKS.put(material, block);
        LADDER_BLOCK_ITEMS.put(material, blockItem);
    }
}
