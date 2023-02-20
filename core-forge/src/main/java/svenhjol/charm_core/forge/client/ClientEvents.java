package svenhjol.charm_core.forge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import svenhjol.charm_api.event.*;
import svenhjol.charm_core.iface.IClientInitializer;
import svenhjol.charm_core.iface.IEvents;

public class ClientEvents implements IEvents {
    private final IClientInitializer init;
    private final ClientDeferred deferred;
    private final ClientRegistry registry;
    private static boolean hasInitializedSingletons = false;

    public ClientEvents(IClientInitializer init, IEventBus modEventBus) {
        this.init = init;
        this.registry = (ClientRegistry)init.getRegistry();
        this.deferred = registry.getDeferred();

        modEventBus.addListener(this::handleRegisterEntityRenderers);
        modEventBus.addListener(this::handleRegisterLayerDefinitions);
        modEventBus.addListener(this::handleRegisterRecipeBookCategories);
        modEventBus.addListener(this::handleRegisterKeyMappings);
        modEventBus.addListener(this::handleRegisterItemColorHandlers);
        modEventBus.addListener(this::handleRegisterBlockColorHandlers);
        modEventBus.addListener(this::handleRegisterParticleProviders);
        modEventBus.addListener(this::handleCreativeModeTabBuildContents);

        if (!hasInitializedSingletons) {
            MinecraftForge.EVENT_BUS.addListener(this::handleClientTick);
            MinecraftForge.EVENT_BUS.addListener(this::handleClientEntityJoinLevel);
            MinecraftForge.EVENT_BUS.addListener(this::handleClientEntityLeaveLevel);
            MinecraftForge.EVENT_BUS.addListener(this::handleKeyboardPresses);
            MinecraftForge.EVENT_BUS.addListener(this::handleMouseScrollingOffScreen);
            MinecraftForge.EVENT_BUS.addListener(this::handleMouseScrollingOnScreen);
            MinecraftForge.EVENT_BUS.addListener(this::handleScreenInit);
            MinecraftForge.EVENT_BUS.addListener(this::handleContainerScreen);
            MinecraftForge.EVENT_BUS.addListener(this::handleRenderGui);
            MinecraftForge.EVENT_BUS.addListener(this::handleRenderHand);
            MinecraftForge.EVENT_BUS.addListener(this::handleRenderTooltip);
            MinecraftForge.EVENT_BUS.addListener(this::handleItemTooltip);
            MinecraftForge.EVENT_BUS.addListener(this::handleSoundSource);
            hasInitializedSingletons = true;
        }
    }

    public void doFinalTasks() {
        deferred.handleDeferredBlockRenderTypes();
        deferred.handleDeferredMenuScreens();
        deferred.handleDeferredSignMaterials();
        deferred.handleDeferredItemProperties();
    }

    private void handleCreativeModeTabBuildContents(CreativeModeTabEvent.BuildContents event) {
        deferred.handleDeferredItemTabs(event);
    }

    private void handleContainerScreen(ContainerScreenEvent.Render.Foreground event) {
        ScreenRenderEvent.INSTANCE.getHandlers().forEach(
            handler -> handler.run(event.getContainerScreen(), event.getPoseStack(), event.getMouseX(), event.getMouseY()));
    }

    /**
     * Maps to the HudRenderEvent in Charm.
     * @param event The RenderGuiEvent.Post Forge event.
     */
    private void handleRenderGui(RenderGuiEvent.Post event) {
        HudRenderEvent.INSTANCE.getHandlers().forEach(
            handler -> handler.run(event.getPoseStack(), event.getPartialTick()));
    }

    /**
     * Maps to the SetupScreenEvent in Charm.
     * @param event The ScreenEvent.Init.Post Forge event.
     */
    private void handleScreenInit(ScreenEvent.Init.Post event) {
        ScreenSetupEvent.INSTANCE.getHandlers().forEach(
            handler -> handler.run(event.getScreen()));
    }

    private void handleClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ClientTickEvent.INSTANCE.getHandlers().forEach(
                handler -> handler.run(Minecraft.getInstance()));
        }
    }

    private void handleClientEntityLeaveLevel(EntityLeaveLevelEvent event) {
        if (event.getLevel() instanceof ClientLevel) {
            ClientEntityLeaveEvent.INSTANCE.invoke(event.getEntity(), event.getLevel());
        }
    }

    private void handleClientEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ClientLevel) {
            ClientEntityJoinEvent.INSTANCE.invoke(event.getEntity(), event.getLevel());
        }
    }

    private void handleSoundSource(SoundEvent.SoundSourceEvent event) {
        SoundPlayEvent.INSTANCE.invoke(event.getEngine(), event.getSound());
    }

    private void handleRenderHand(RenderHandEvent event) {
        var handlers = HeldItemRenderEvent.INSTANCE.getHandlers();

        for (var handler : handlers) {
            var result = handler.run(
                event.getPartialTick(),
                event.getInterpolatedPitch(),
                event.getHand(),
                event.getSwingProgress(),
                event.getItemStack(),
                event.getEquipProgress(),
                event.getPoseStack(),
                event.getMultiBufferSource(),
                event.getPackedLight());

            if (result != InteractionResult.PASS) {
                event.setCanceled(true);
                return;
            }
        }
    }

    private void handleRenderTooltip(RenderTooltipEvent.Pre event) {
        TooltipRenderEvent.INSTANCE.invoke(event.getPoseStack(), event.getComponents(), event.getX(), event.getY(), event.getItemStack());
    }

    private void handleItemTooltip(ItemTooltipEvent event) {
        TooltipItemHoverEvent.INSTANCE.invoke(event.getItemStack(), event.getToolTip(), event.getFlags());
    }

    private void handleKeyboardPresses(TickEvent.ClientTickEvent event) {
        var handlers = KeyPressEvent.INSTANCE.getHandlers();
        var mappings = registry.getKeyMappings();

        if (event.phase == TickEvent.Phase.END) {
            for (var id : mappings.keySet()) {
                var mapping = mappings.get(id);
                while (mapping.get().consumeClick()) {
                    handlers.forEach(handler -> handler.run(id));
                }
            }
        }
    }

    private void handleMouseScrollingOffScreen(InputEvent.MouseScrollingEvent event) {
        MouseScrollEvent.OFF_SCREEN.invoke(event.getScrollDelta());
    }

    private void handleMouseScrollingOnScreen(ScreenEvent.MouseScrolled.Pre event) {
        MouseScrollEvent.ON_SCREEN.invoke(event.getScrollDelta());
    }

    public void handleRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        deferred.handleDeferredEntityRenderers(event);
    }

    public void handleRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        deferred.handleDeferredModelLayerDefinitions(event);
    }

    public void handleRegisterRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        deferred.handleDeferredRecipeCategories(event);
    }

    public void handleRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        for (var mapping : registry.getKeyMappings().values()) {
            event.register(mapping.get());
        }
    }

    public void handleRegisterItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        deferred.handleDeferredItemColors(event);
    }

    public void handleRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        deferred.handleDeferredBlockColors(event);
    }

    public void handleRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        deferred.handleDeferredParticleProviders(event);
    }
}
