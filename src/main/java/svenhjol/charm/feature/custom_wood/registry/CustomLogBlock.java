package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmLogBlock;

import java.util.function.Supplier;

public class CustomLogBlock {
    public final Supplier<CharmLogBlock> block;
    public final Supplier<CharmLogBlock.BlockItem> item;
    public final Supplier<CharmLogBlock> strippedBlock;
    public final Supplier<CharmLogBlock.BlockItem> strippedItem;

    public CustomLogBlock(CustomWoodHolder holder) {
        var material = holder.getMaterial();
        var feature = holder.getFeature();
        var registry = holder.getRegistry();

        var id = holder.getMaterialName() + "_log";
        var strippedId = "stripped_" + holder.getMaterialName() + "_log";

        block = registry.block(id, () -> new CharmLogBlock(feature, material));
        item = registry.item(id, () -> new CharmLogBlock.BlockItem(feature, block));
        strippedBlock = registry.block(strippedId, () -> new CharmLogBlock(feature, material));
        strippedItem = registry.item(strippedId, () -> new CharmLogBlock.BlockItem(feature, strippedBlock));

        // Logs can set on fire.
        registry.ignite(block);
        registry.ignite(strippedBlock);

        // Logs can be stripped.
        registry.strippable(block, strippedBlock);

        holder.addCreativeTabItem(CustomWoodHelper.LOGS, item);
        holder.addCreativeTabItem(CustomWoodHelper.STRIPPED_LOGS, strippedItem);
    }
}
