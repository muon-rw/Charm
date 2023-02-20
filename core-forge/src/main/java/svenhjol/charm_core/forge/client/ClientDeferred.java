package svenhjol.charm_core.forge.client;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import svenhjol.charm_core.forge.registry.*;
import svenhjol.charm_core.iface.IClientInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

@SuppressWarnings("removal")
public class ClientDeferred {
    private final IClientInitializer init;
    public final List<DeferredBlockEntityRenderer<BlockEntity>> blockEntityRenderers = new ArrayList<>();
    public final List<DeferredItemColor> itemColors = new ArrayList<>();
    public final List<DeferredBlockColor> blockColors = new ArrayList<>();
    public final List<DeferredMenuScreen<AbstractContainerMenu, ?>> menuScreens = new ArrayList<>();
    public final List<DeferredBlockRenderType<? extends Block>> blockRenderTypes = new ArrayList<>();
    public final List<DeferredEntityRenderer<Entity>> entityRenderers = new ArrayList<>();
    public final List<DeferredModelLayerDefinition> modelLayerDefinitions = new ArrayList<>();
    public final List<DeferredRecipeCategory<Recipe<?>>> recipeCategories = new ArrayList<>();
    public final List<DeferredRecipeCategoryEnum> recipeBookCategoryEnums = new ArrayList<>();
    public final List<DeferredSignMaterial> signMaterials = new ArrayList<>();
    public final List<DeferredParticleProvider> particleProviders = new ArrayList<>();
    public final List<DeferredItemProperty> itemProperties = new ArrayList<>();
    public final List<DeferredItemTab> itemTabs = new ArrayList<>();

    public ClientDeferred(IClientInitializer init) {
        this.init = init;
    }

    public void handleDeferredEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (var deferred : blockEntityRenderers) {
            event.registerBlockEntityRenderer(deferred.type().get(), deferred.provider().get());
        }

        for (var deferred : entityRenderers) {
            event.registerEntityRenderer(deferred.entity().get(), deferred.provider().get());
        }
    }

    public void handleDeferredModelLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (var deferred : modelLayerDefinitions) {
            var modelLayerLocation = deferred.location().get();
            init.getLog().debug(ClientRegistry.class, "Registering model layer location " + modelLayerLocation.toString());
            event.registerLayerDefinition(modelLayerLocation, deferred.definition());
        }
    }

    public void handleDeferredMenuScreens() {
        for (var deferred : menuScreens) {
            var menuType = deferred.menuType().get();
            init.getLog().debug(ClientRegistry.class, "Registering menu type " + menuType.toString());
            MenuScreens.register(deferred.menuType().get(), deferred.screenConstructor().get());
        }
    }

    public void handleDeferredBlockRenderTypes() {
        for (var deferred : blockRenderTypes) {
            var block = deferred.block().get();
            init.getLog().debug(ClientRegistry.class, "Registering block render type " + block.toString());
            ItemBlockRenderTypes.setRenderLayer(deferred.block().get(), deferred.renderType().get());
        }
    }

    public void handleDeferredItemTabs(CreativeModeTabEvent.BuildContents event) {
        for (var deferred : itemTabs) {
            if (event.getTab().equals(deferred.tab())) {
                event.accept(deferred.item());
            }
        }
    }

    public void handleDeferredItemColors(RegisterColorHandlersEvent.Item event) {
        for (var deferred : itemColors) {
            event.register(deferred.itemColor(), deferred.items().stream()
                .map(Supplier::get)
                .toList()
                .toArray(new ItemLike[0]));
        }
    }

    public void handleDeferredBlockColors(RegisterColorHandlersEvent.Block event) {
        for (var deferred : blockColors) {
            event.register(deferred.blockColor(), deferred.blocks().stream()
                .map(Supplier::get)
                .toList()
                .toArray(new Block[0]));
        }
    }

    public void handleDeferredRecipeCategories(RegisterRecipeBookCategoriesEvent event) {
        // Add enums here so we can use Forge-registered items and blocks for the menuIcon.
        for (var deferred : recipeBookCategoryEnums) {
            RecipeBookCategories.create(
                deferred.name().toUpperCase(Locale.ROOT),
                new ItemStack(deferred.menuIcon().get()));
        }

        for (var deferred : recipeCategories) {
            var id = deferred.id().toUpperCase(Locale.ROOT);
            var recipeType = deferred.recipeType().get();
            var recipeBookType = deferred.recipeBookType().get();
            var searchCategory = RecipeBookCategories.valueOf(id + "_SEARCH");
            var mainCategory = RecipeBookCategories.valueOf(id);

            event.registerAggregateCategory(searchCategory, List.of(mainCategory));
            event.registerBookCategories(recipeBookType, List.of(searchCategory, mainCategory));
            event.registerRecipeCategoryFinder(recipeType, recipe -> mainCategory);
        }
    }

    public void handleDeferredSignMaterials() {
        for (var deferred : signMaterials) {
            var woodType = deferred.woodType().get();
            Sheets.SIGN_MATERIALS.put(woodType, new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/" + woodType.name())));
        }
    }

    public void handleDeferredParticleProviders(RegisterParticleProvidersEvent event) {
        for (var deferred : particleProviders) {
            event.register(deferred.particleType().get(), deferred.particleProvider().get());
        }
    }

    public void handleDeferredItemProperties() {
        for (var deferred : itemProperties) {
            ItemProperties.register(deferred.item().get(), deferred.id(), deferred.function().get());
        }
    }
}
