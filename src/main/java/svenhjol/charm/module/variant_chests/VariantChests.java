package svenhjol.charm.module.variant_chests;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.enums.VanillaVariantMaterial;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Module;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, priority = 10, client = VariantChestsClient.class, description = "Chests available in all types of vanilla wood.",
    requiresMixins = {"StitchTextureCallback", "RenderBlockItemCallback"})
public class VariantChests extends CharmModule {
    public static final Identifier NORMAL_ID = new Identifier(Charm.MOD_ID, "variant_chest");
    public static final Identifier TRAPPED_ID = new Identifier(Charm.MOD_ID, "trapped_chest");

    public static final Map<IVariantMaterial, VariantChestBlock> NORMAL_CHEST_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, VariantTrappedChestBlock> TRAPPED_CHEST_BLOCKS = new HashMap<>();

    public static BlockEntityType<VariantChestBlockEntity> NORMAL_BLOCK_ENTITY;
    public static BlockEntityType<VariantTrappedChestBlockEntity> TRAPPED_BLOCK_ENTITY;

    @Override
    public void register() {
        NORMAL_BLOCK_ENTITY = RegistryHelper.blockEntity(NORMAL_ID, VariantChestBlockEntity::new, NORMAL_CHEST_BLOCKS.values().toArray(new Block[0]));
        TRAPPED_BLOCK_ENTITY = RegistryHelper.blockEntity(TRAPPED_ID, VariantTrappedChestBlockEntity::new, TRAPPED_CHEST_BLOCKS.values().toArray(new Block[0]));

        for (VanillaVariantMaterial type : VanillaVariantMaterial.values()) {
            registerChest(this, type);
            registerTrappedChest(this, type);
        }
    }

    public static VariantChestBlock registerChest(CharmModule module, IVariantMaterial material) {
        VariantChestBlock chest = new VariantChestBlock(module, material);
        NORMAL_CHEST_BLOCKS.put(material, chest);
        RegistryHelper.addBlocksToBlockEntity(NORMAL_BLOCK_ENTITY, chest);
        return chest;
    }

    public static VariantTrappedChestBlock registerTrappedChest(CharmModule module, IVariantMaterial material) {
        VariantTrappedChestBlock chest = new VariantTrappedChestBlock(module, material);
        TRAPPED_CHEST_BLOCKS.put(material, chest);
        RegistryHelper.addBlocksToBlockEntity(TRAPPED_BLOCK_ENTITY, chest);
        return chest;
    }
}