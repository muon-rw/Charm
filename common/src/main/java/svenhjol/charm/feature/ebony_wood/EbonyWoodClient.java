package svenhjol.charm.feature.ebony_wood;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class EbonyWoodClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(EbonyWood.class));
    }

    @Override
    public void register() {
        // Cut out transparent areas of blocks.
        CharmClient.REGISTRY.blockRenderType(EbonyWood.DOOR_BLOCK, RenderType::cutout);
        CharmClient.REGISTRY.blockRenderType(EbonyWood.TRAPDOOR_BLOCK, RenderType::cutout);
        CharmClient.REGISTRY.blockRenderType(EbonyWood.SAPLING_BLOCK, RenderType::cutout);

        // Register boat models.
        CharmClient.REGISTRY.modelLayer(
            () -> new ModelLayerLocation(Charm.makeId("boat/ebony"), "main"),
            BoatModel::createBodyModel);

        CharmClient.REGISTRY.modelLayer(
            () -> new ModelLayerLocation(Charm.makeId("chest_boat/ebony"), "main"),
            ChestBoatModel::createBodyModel);

        // Register sign material.
        CharmClient.REGISTRY.signMaterial(EbonyWood.WOOD_TYPE);

        // Register foliage colors.
        CharmClient.REGISTRY.itemColor(this::handleItemColor, List.of(EbonyWood.LEAVES_BLOCK));
        CharmClient.REGISTRY.blockColor(this::handleBlockColor, List.of(EbonyWood.LEAVES_BLOCK));
    }

    private int handleItemColor(ItemStack stack, int tintIndex) {
        var state = ((BlockItem)stack.getItem()).getBlock().defaultBlockState();
        var blockColors = Minecraft.getInstance().getBlockColors();
        return blockColors.getColor(state, null, null, tintIndex);
    }

    private int handleBlockColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
        return level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.getDefaultColor();
    }
}
