package svenhjol.charm.feature.variant_chiseled_bookshelves;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_chiseled_bookshelves.VariantChiseledBookshelfBlock.BlockItem;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.iface.IRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, switchable = false, description = "Register variant chiseled bookshelves.")
public class VariantChiseledBookshelves extends CharmFeature {
    static final Map<IVariantMaterial, Supplier<VariantChiseledBookshelfBlock>> CHISELED_BOOKSHELF_BLOCKS = new HashMap<>();
    static final Map<IVariantMaterial, Supplier<VariantChiseledBookshelfBlock.BlockItem>> CHISELED_BOOKSHELF_BLOCK_ITEMS = new HashMap<>();

    public static void registerChiseledBookshelf(IRegistry registry, IVariantWoodMaterial material) {
        var id = "chiseled_" + material.getSerializedName() + "_bookshelf";
        var block = registry.block(id, () -> new VariantChiseledBookshelfBlock(material));
        var blockItem = registry.item(id, () -> new BlockItem(block));

        CHISELED_BOOKSHELF_BLOCKS.put(material, block);
        CHISELED_BOOKSHELF_BLOCK_ITEMS.put(material, blockItem);

        registry.fuel(blockItem);

        // Associate blocks with block entity dynamically.
        registry.blockEntityBlocks(() -> BlockEntityType.CHISELED_BOOKSHELF, List.of(block));
    }
}
