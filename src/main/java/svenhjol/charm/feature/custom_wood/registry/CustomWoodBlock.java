package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmLogBlock;

import java.util.function.Supplier;

public class CustomWoodBlock {
    public final Supplier<CharmLogBlock> block;
    public final Supplier<CharmLogBlock.BlockItem> item;
    public final Supplier<CharmLogBlock> strippedBlock;
    public final Supplier<CharmLogBlock.BlockItem> strippedItem;

    public CustomWoodBlock(CustomWoodHolder holder) {
        var material = holder.getMaterial();
        var feature = holder.getFeature();
        var registry = holder.getRegistry();

        var woodId = holder.getMaterialName() + "_wood";
        var strippedWoodId = "stripped_" + holder.getMaterialName() + "_wood";

        block = registry.block(woodId, () -> new CharmLogBlock(feature, material));
        item = registry.item(woodId, () -> new CharmLogBlock.BlockItem(feature, block));
        strippedBlock = registry.block(strippedWoodId, () -> new CharmLogBlock(feature, material));
        strippedItem = registry.item(strippedWoodId, () -> new CharmLogBlock.BlockItem(feature, strippedBlock));

        // Wood can set on fire.
        registry.ignite(block);
        registry.ignite(strippedBlock);

        // Wood can be stripped.
        registry.strippable(block, strippedBlock);

        holder.addCreativeTabItem(CustomWoodHelper.WOODS, item);
        holder.addCreativeTabItem(CustomWoodHelper.STRIPPED_WOODS, strippedItem);
    }
}
