package svenhjol.charm.feature.variant_chests;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_chests.VariantTrappedChestBlock.BlockItem;
import svenhjol.charm_api.event.EntityUseEvent;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, priority = 10, switchable = false, description = "Registers variant chests and trapped chests.")
public class VariantChests extends CharmFeature {
    public static final Map<IVariantMaterial, Supplier<VariantChestBlock>> NORMAL_CHEST_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, Supplier<VariantChestBlock.BlockItem>> NORMAL_CHEST_BLOCK_ITEMS = new HashMap<>();
    public static final Map<IVariantMaterial, Supplier<VariantTrappedChestBlock>> TRAPPED_CHEST_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, Supplier<BlockItem>> TRAPPED_CHEST_BLOCK_ITEMS = new HashMap<>();
    public static Supplier<BlockEntityType<VariantChestBlockEntity>> NORMAL_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<VariantChestBlockEntity>> TRAPPED_BLOCK_ENTITY;

    @Override
    public void register() {
        NORMAL_BLOCK_ENTITY = Charm.REGISTRY.blockEntity("variant_chest", () -> VariantChestBlockEntity::new);
        TRAPPED_BLOCK_ENTITY = Charm.REGISTRY.blockEntity("variant_trapped_chest", () -> VariantTrappedChestBlockEntity::new);
    }

    @Override
    public void runWhenEnabled() {
        // Handle interactions with llamas, donkeys.
        EntityUseEvent.INSTANCE.handle(AnimalInteraction::handle);
    }

    public static void registerChest(IVariantMaterial material) {
        var id = material.getSerializedName() + "_chest";

        var block = Charm.REGISTRY.block(id, () -> new VariantChestBlock(material));
        var blockItem = Charm.REGISTRY.item(id, () -> new VariantChestBlock.BlockItem(block));

        NORMAL_CHEST_BLOCKS.put(material, block);
        NORMAL_CHEST_BLOCK_ITEMS.put(material, blockItem);

        // Associate the blocks with the block entity dynamically.
        Charm.REGISTRY.blockEntityBlocks(NORMAL_BLOCK_ENTITY, List.of(block));
        Charm.REGISTRY.fuel(blockItem);
    }

    public static void registerTrappedChest(IVariantMaterial material) {
        var id = material.getSerializedName() + "_trapped_chest";

        var block = Charm.REGISTRY.block(id, () -> new VariantTrappedChestBlock(material));
        var blockItem = Charm.REGISTRY.item(id, () -> new BlockItem(block));

        TRAPPED_CHEST_BLOCKS.put(material, block);
        TRAPPED_CHEST_BLOCK_ITEMS.put(material, blockItem);

        // Associate the blocks with the block entity dynamically.
        Charm.REGISTRY.blockEntityBlocks(TRAPPED_BLOCK_ENTITY, List.of(block));
        Charm.REGISTRY.fuel(blockItem);
    }
}
