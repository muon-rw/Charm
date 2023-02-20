package svenhjol.charm_core.forge.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
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
import net.minecraftforge.common.util.Lazy;
import svenhjol.charm_core.forge.registry.*;
import svenhjol.charm_core.iface.IClientInitializer;
import svenhjol.charm_core.iface.IClientRegistry;
import svenhjol.charm_core.iface.ILog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ClientRegistry implements IClientRegistry {
    private final IClientInitializer init;
    private final ClientDeferred deferred;
    private final Map<String, Lazy<KeyMapping>> keyMappings = new HashMap<>();

    public ClientRegistry(IClientInitializer init) {
        this.init = init;
        this.deferred = new ClientDeferred(init);
    }

    public ClientDeferred getDeferred() {
        return deferred;
    }

    @Override
    public ResourceLocation makeId(String id) {
        return !id.contains(":") ? new ResourceLocation(getNamespace(), id) : new ResourceLocation(id);
    }

    @Override
    public void blockColor(BlockColor blockColor, List<Supplier<? extends Block>> blocks) {
        deferred.blockColors.add(new DeferredBlockColor(blockColor, blocks));
    }

    @Override
    public <T extends BlockEntity> void blockEntityRenderer(Supplier<BlockEntityType<T>> supplier, Supplier<BlockEntityRendererProvider<T>> provider) {
        deferred.blockEntityRenderers.add((DeferredBlockEntityRenderer<BlockEntity>) new DeferredBlockEntityRenderer<>(supplier, provider));
    }

    @Override
    public <T extends Block> void blockRenderType(Supplier<T> block, Supplier<RenderType> renderType) {
        deferred.blockRenderTypes.add(new DeferredBlockRenderType<>(block, renderType));
    }

    @Override
    public <T extends Entity> void entityRenderer(Supplier<EntityType<T>> entity, Supplier<EntityRendererProvider<T>> provider) {
        deferred.entityRenderers.add(new DeferredEntityRenderer(entity, provider));
    }

    @Override
    public void itemColor(ItemColor itemColor, List<Supplier<? extends ItemLike>> items) {
        deferred.itemColors.add(new DeferredItemColor(itemColor, items));
    }

    @Override
    public <T extends Item> void itemProperties(String id, Supplier<T> item, Supplier<ItemPropertyFunction> function) {
        deferred.itemProperties.add(new DeferredItemProperty(makeId(id), item, function));
    }

    @Override
    public <T extends ItemLike> void itemTab(Supplier<T> item, CreativeModeTab tab, ItemLike showAfter) {
        deferred.itemTabs.add(new DeferredItemTab((Supplier<Item>)item, tab, showAfter));
    }

    @Override
    public Supplier<String> key(String id, Supplier<KeyMapping> supplier) {
        getLog().debug(getClass(), "Registering key mapping " + id);
        keyMappings.put(id, Lazy.of(supplier));
        return () -> id;
    }

    @Override
    public Supplier<ModelLayerLocation> modelLayer(Supplier<ModelLayerLocation> location, Supplier<LayerDefinition> definition) {
        deferred.modelLayerDefinitions.add(new DeferredModelLayerDefinition(location, definition));
        return location;
    }

    @Override
    public <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void menuScreen(Supplier<MenuType<T>> menuType, Supplier<MenuScreens.ScreenConstructor<T, U>> screenConstructor) {
        deferred.menuScreens.add((DeferredMenuScreen<AbstractContainerMenu, ?>) new DeferredMenuScreen<>(menuType, screenConstructor));
    }

    @Override
    public Supplier<SpriteParticleRegistration<SimpleParticleType>> particle(Supplier<SimpleParticleType> particleType, Supplier<SpriteParticleRegistration<SimpleParticleType>> particleProvider) {
        deferred.particleProviders.add(new DeferredParticleProvider(particleType, particleProvider));
        return particleProvider;
    }

    @Override
    public <R extends Recipe<?>> void recipeBookCategory(String id, Supplier<RecipeType<R>> recipeType, Supplier<RecipeBookType> recipeBookType) {
        deferred.recipeCategories.add(new DeferredRecipeCategory(id, recipeType, recipeBookType));
    }

    public void recipeBookCategoryEnum(String name, Supplier<? extends ItemLike> menuIcon) {
        deferred.recipeBookCategoryEnums.add(new DeferredRecipeCategoryEnum(name, menuIcon));
    }

    @Override
    public void resourcePack(String id, boolean enabledByDefault) {
        // ummm
    }

    @Override
    public void signMaterial(Supplier<WoodType> woodType) {
        deferred.signMaterials.add(new DeferredSignMaterial(woodType));
    }

    public Map<String, Lazy<KeyMapping>> getKeyMappings() {
        return keyMappings;
    }

    @Override
    public String getNamespace() {
        return init.getRegistryNamespace();
    }

    @Override
    public ILog getLog() {
        return init.getLog();
    }
}
