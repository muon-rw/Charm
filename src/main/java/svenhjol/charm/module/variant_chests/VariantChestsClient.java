package svenhjol.charm.module.variant_chests;

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.event.RenderBlockItemCallback;
import svenhjol.charm.event.StitchTextureCallback;

import java.util.Set;

public class VariantChestsClient extends CharmClientModule {
    private final VariantChestBlockEntity CACHED_NORMAL_CHEST = new VariantChestBlockEntity(BlockPos.ORIGIN, Blocks.CHEST.getDefaultState());
    private final VariantTrappedChestBlockEntity CACHED_TRAPPED_CHEST = new VariantTrappedChestBlockEntity(BlockPos.ORIGIN, Blocks.TRAPPED_CHEST.getDefaultState());

    public VariantChestsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        BlockEntityRendererRegistry.INSTANCE.register(VariantChests.NORMAL_BLOCK_ENTITY, VariantChestBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(VariantChests.TRAPPED_BLOCK_ENTITY, VariantChestBlockEntityRenderer::new);

        StitchTextureCallback.EVENT.register(this::handleTextureStitch);
        RenderBlockItemCallback.EVENT.register(this::handleBlockItemRender);
    }

    private void handleTextureStitch(SpriteAtlasTexture atlas, Set<Identifier> textures) {
        if (atlas.getId().toString().equals("minecraft:textures/atlas/chest.png")) {
            VariantChests.NORMAL_CHEST_BLOCKS.keySet().forEach(type -> {
                addChestTexture(textures, type, ChestType.LEFT);
                addChestTexture(textures, type, ChestType.RIGHT);
                addChestTexture(textures, type, ChestType.SINGLE);
            });
        }
    }

    private BlockEntity handleBlockItemRender(Block block) {
        if (block instanceof VariantChestBlock) {
            VariantChestBlock chest = (VariantChestBlock)block;
            CACHED_NORMAL_CHEST.setMaterialType(chest.getMaterialType());
            return CACHED_NORMAL_CHEST;

        } else if (block instanceof VariantTrappedChestBlock) {
            VariantTrappedChestBlock chest = (VariantTrappedChestBlock)block;
            CACHED_TRAPPED_CHEST.setMaterialType(chest.getMaterialType());
            return CACHED_TRAPPED_CHEST;
        }

        return null;
    }

    private void addChestTexture(Set<Identifier> textures, IVariantMaterial variant, ChestType chestType) {
        String chestTypeName = chestType != ChestType.SINGLE ? "_" + chestType.asString().toLowerCase() : "";
        String[] bases = {"trapped", "normal"};

        for (String base : bases) {
            Identifier id = new Identifier(module.mod, "entity/chest/" + variant.asString() + "_" + base + chestTypeName);
            VariantChestBlockEntityRenderer.addTexture(variant, chestType, id, base.equals("trapped"));
            textures.add(id);
        }
    }
}