package svenhjol.charm_core.fabric.client;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm_core.fabric.common.CommonRegistry;
import svenhjol.charm_core.fabric.registry.DeferredParticle;
import svenhjol.charm_core.iface.IClientInitializer;
import svenhjol.charm_core.iface.IClientRegistry;
import svenhjol.charm_core.iface.ILog;
import svenhjol.charm_core.iface.IRegistry;
import svenhjol.charm_core.mixin.accessor.RecipeBookCategoriesAccessor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class ClientRegistry implements IClientRegistry {
    private final IClientInitializer init;
    private final Map<String, KeyMapping> keyMappings = new HashMap<>();
    private static final List<Pair<String, ItemLike>> RECIPE_BOOK_CATEGORY_ENUMS = new ArrayList<>();
    private static final Map<RecipeBookType, List<RecipeBookCategories>> RECIPE_BOOK_CATEGORY_BY_TYPE = new HashMap<>();
    private static final Map<RecipeType<?>, RecipeBookCategories> RECIPE_BOOK_MAIN_CATEGORY = new HashMap<>();

    public ClientRegistry(IClientInitializer init) {
        this.init = init;
    }

    @Override
    public ResourceLocation makeId(String id) {
        return !id.contains(":") ? new ResourceLocation(getNamespace(), id) : new ResourceLocation(id);
    }

    @Override
    public void blockColor(BlockColor blockColor, List<Supplier<? extends Block>> blocks) {
        ColorProviderRegistry.BLOCK.register(blockColor, blocks.stream().map(Supplier::get).toList().toArray(Block[]::new));
    }

    @Override
    public <T extends BlockEntity> void blockEntityRenderer(Supplier<BlockEntityType<T>> supplier, Supplier<BlockEntityRendererProvider<T>> provider) {
        BlockEntityRendererRegistry.register(supplier.get(), provider.get());
    }

    @Override
    public <T extends Block> void blockRenderType(Supplier<T> block, Supplier<RenderType> renderType) {
        BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderType.get());
    }

    @Override
    public <T extends Entity> void entityRenderer(Supplier<EntityType<T>> entity, Supplier<EntityRendererProvider<T>> provider) {
        EntityRendererRegistry.register(entity.get(), provider.get());
    }

    @Override
    public void itemColor(ItemColor itemColor, List<Supplier<? extends ItemLike>> items) {
        ColorProviderRegistry.ITEM.register(itemColor, items.stream().map(Supplier::get).toList().toArray(ItemLike[]::new));
    }

    @Override
    public <T extends Item> void itemProperties(String id, Supplier<T> item, Supplier<ItemPropertyFunction> function) {
        var itemPropertyFunction = function.get();
        var clampedItemPropertyFunction = new ClampedItemPropertyFunction() {
            @Override
            public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                return itemPropertyFunction.call(itemStack, clientLevel, livingEntity, i);
            }

            @Override
            public float call(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                return itemPropertyFunction.call(itemStack, clientLevel, livingEntity, i);
            }
        };
        ItemProperties.register(item.get(), makeId(id), clampedItemPropertyFunction);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public <T extends ItemLike> void itemTab(Supplier<T> item, CreativeModeTab tab, @Nullable ItemLike showAfter) {
        if (showAfter != null) {
            ItemGroupEvents.modifyEntriesEvent(tab)
                .register(entries -> entries.addAfter(showAfter, item.get()));
        } else {
            ItemGroupEvents.modifyEntriesEvent(tab)
                .register(entries -> entries.accept(item.get()));
        }
    }

    @Override
    public Supplier<String> key(String id, Supplier<KeyMapping> supplier) {
        getLog().debug(getClass(), "Registering key mapping " + id);
        var mapping = KeyBindingHelper.registerKeyBinding(supplier.get());
        keyMappings.put(id, mapping);
        return () -> id;
    }

    @Override
    public Supplier<ModelLayerLocation> modelLayer(Supplier<ModelLayerLocation> location, Supplier<LayerDefinition> definition) {
        EntityModelLayerRegistry.registerModelLayer(location.get(), definition::get);
        return location;
    }

    @Override
    public <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void menuScreen(Supplier<MenuType<T>> menuType, Supplier<MenuScreens.ScreenConstructor<T, U>> screenConstructor) {
        MenuScreens.register(menuType.get(), screenConstructor.get());
    }

    @Override
    public Supplier<ParticleEngine.SpriteParticleRegistration<SimpleParticleType>> particle(Supplier<SimpleParticleType> particleType, Supplier<ParticleEngine.SpriteParticleRegistration<SimpleParticleType>> particleProvider) {
        ClientDeferred.particles.add(new DeferredParticle(particleType, particleProvider));
        return particleProvider;
    }

    @Override
    public <R extends Recipe<?>> void recipeBookCategory(String id, Supplier<RecipeType<R>> recipeType, Supplier<RecipeBookType> recipeBookType) {
        var upper = id.toUpperCase(Locale.ROOT);
        var searchCategory = RecipeBookCategories.valueOf(upper + "_SEARCH");
        var mainCategory = RecipeBookCategories.valueOf(upper);

        RECIPE_BOOK_MAIN_CATEGORY.put(recipeType.get(), mainCategory);
        RECIPE_BOOK_CATEGORY_BY_TYPE.put(recipeBookType.get(), List.of(searchCategory, mainCategory));

        var aggregateCategories = new HashMap<>(RecipeBookCategoriesAccessor.getAggregateCategories());
        aggregateCategories.put(searchCategory, List.of(mainCategory));
        RecipeBookCategoriesAccessor.setAggregateCategories(aggregateCategories);
    }

    @Override
    public void recipeBookCategoryEnum(String name, Supplier<? extends ItemLike> menuIcon) {
        RECIPE_BOOK_CATEGORY_ENUMS.add(Pair.of(name, menuIcon.get()));
    }

    @Override
    public void resourcePack(String id, boolean enabledByDefault) {
        var packId = new ResourceLocation(getNamespace(), id);
        FabricLoader.getInstance().getModContainer(getNamespace()).ifPresent(container
            -> ResourceManagerHelper.registerBuiltinResourcePack(packId, container,
               enabledByDefault ? ResourcePackActivationType.DEFAULT_ENABLED : ResourcePackActivationType.NORMAL));
    }

    @Override
    public void signMaterial(Supplier<WoodType> woodType) {
        Sheets.SIGN_MATERIALS.put(woodType.get(), new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/" + woodType.get().name())));
        Sheets.HANGING_SIGN_MATERIALS.put(woodType.get(), new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/hanging/" + woodType.get().name())));
    }

    @Override
    public String getNamespace() {
        return init.getRegistryNamespace();
    }

    @Override
    public ILog getLog() {
        return init.getLog();
    }

    public void handleDeferredClientPackets(IRegistry registry) {
        ((CommonRegistry)registry).getClientPackets().forEach(
            (packet, handler) -> ClientPlayNetworking.registerGlobalReceiver(packet.id(),
                (client, listener, buf, sender) -> {
                    packet.decode(buf);
                    client.execute(() -> handler.get().handle(packet, Minecraft.getInstance().player));
                }));
    }

    public Map<String, KeyMapping> getKeyMappings() {
        return keyMappings;
    }

    public static Map<RecipeType<?>, RecipeBookCategories> getRecipeBookMainCategory() {
        return RECIPE_BOOK_MAIN_CATEGORY;
    }

    public static List<Pair<String, ItemLike>> getRecipeBookCategoryEnums() {
        return RECIPE_BOOK_CATEGORY_ENUMS;
    }

    public static Map<RecipeBookType, List<RecipeBookCategories>> getRecipeBookCategoryByType() {
        return RECIPE_BOOK_CATEGORY_BY_TYPE;
    }

    public static List<DeferredParticle> getParticles() {
        return ClientDeferred.particles;
    }
}