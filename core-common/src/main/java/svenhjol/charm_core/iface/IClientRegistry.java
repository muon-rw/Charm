package svenhjol.charm_core.iface;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public interface IClientRegistry {
    ResourceLocation makeId(String id);

    String getNamespace();

    ILog getLog();

    void blockColor(BlockColor blockColor, List<Supplier<? extends Block>> blocks);

    <T extends BlockEntity> void blockEntityRenderer(Supplier<BlockEntityType<T>> supplier, Supplier<BlockEntityRendererProvider<T>> provider);

    <T extends Block> void blockRenderType(Supplier<T> block, Supplier<RenderType> renderType);

    <T extends Entity> void entityRenderer(Supplier<EntityType<T>> entity, Supplier<EntityRendererProvider<T>> provider);

    void itemColor(ItemColor itemColor, List<Supplier<? extends ItemLike>> items);

    <T extends Item> void itemProperties(String id, Supplier<T> item, Supplier<ItemPropertyFunction> function);

    <T extends ItemLike> void itemTab(Supplier<T> item, CreativeModeTab tab, @Nullable ItemLike showAfter);

    Supplier<String> key(String id, Supplier<KeyMapping> supplier);

    Supplier<ModelLayerLocation> modelLayer(Supplier<ModelLayerLocation> location, Supplier<LayerDefinition> definition);

    <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void menuScreen(Supplier<MenuType<T>> menuType, Supplier<MenuScreens.ScreenConstructor<T, U>> screenConstructor);

    Supplier<ParticleEngine.SpriteParticleRegistration<SimpleParticleType>> particle(Supplier<SimpleParticleType> particleType, Supplier<ParticleEngine.SpriteParticleRegistration<SimpleParticleType>> particleProvider);

    <R extends Recipe<?>> void recipeBookCategory(String id, Supplier<RecipeType<R>> recipeType, Supplier<RecipeBookType> recipeBookType);

    void recipeBookCategoryEnum(String name, Supplier<? extends ItemLike> menuIcon);

    void resourcePack(String id, boolean enabledByDefault);

    void signMaterial(Supplier<WoodType> woodType);
}
