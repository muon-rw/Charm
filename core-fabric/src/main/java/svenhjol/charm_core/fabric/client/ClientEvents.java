package svenhjol.charm_core.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm_api.event.*;
import svenhjol.charm_core.fabric.event.*;
import svenhjol.charm_core.iface.IClientInitializer;
import svenhjol.charm_core.iface.IEvents;

import java.util.List;
import java.util.function.Function;

public class ClientEvents implements IEvents {
    private final IClientInitializer init;
    private final ClientRegistry registry;
    private static boolean hasInitializedSingletons = false;

    public ClientEvents(IClientInitializer init) {
        this.init = init;
        this.registry = (ClientRegistry) init.getRegistry();

        if (!hasInitializedSingletons) {
            ClientLifecycleEvents.CLIENT_STARTED.register(this::handleClientStarted);
            ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);
            ClientEntityEvents.ENTITY_LOAD.register(this::handleClientEntityLoad);
            ClientEntityEvents.ENTITY_UNLOAD.register(this::handleClientEntityUnload);
            SetupScreenCallback.EVENT.register(this::handleSetupScreen);
            RenderScreenCallback.EVENT.register(this::handleRenderScreen);
            RenderHeldItemCallback.EVENT.register(this::handleRenderHeldItem);
            RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
            HudRenderCallback.EVENT.register(this::handleHudRender);
            StitchTextureCallback.EVENT.register(this::handleStitchTexture);
            PlaySoundCallback.EVENT.register(this::handlePlaySound);

            hasInitializedSingletons = true;
        }

        // Spin up a dedicated listener for key presses.
        ClientTickEvents.END_CLIENT_TICK.register(this::handleKeyPresses);
    }

    private void handleClientEntityUnload(Entity entity, ClientLevel level) {
        ClientEntityLeaveEvent.INSTANCE.invoke(entity, level);
    }

    private void handleClientEntityLoad(Entity entity, ClientLevel level) {
        ClientEntityJoinEvent.INSTANCE.invoke(entity, level);
    }

    private void handleRenderTooltip(Screen screen, PoseStack poseStack, ItemStack itemStack, List<ClientTooltipComponent> lines, int x, int y) {
        TooltipRenderEvent.INSTANCE.invoke(poseStack, lines, x, y, itemStack);
    }

    private void handlePlaySound(SoundEngine soundEngine, SoundInstance soundInstance) {
        SoundPlayEvent.INSTANCE.invoke(soundEngine, soundInstance);
    }

    private void handleClientStarted(Minecraft client) {
        ClientStartEvent.INSTANCE.invoke(client);
    }

    private InteractionResult handleRenderHeldItem(float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equipProgress, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        for (var handler : HeldItemRenderEvent.INSTANCE.getHandlers()) {
            var result = handler.run(tickDelta, pitch, hand, swingProgress, stack, equipProgress, poseStack, bufferSource, light);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }

    private void handleStitchTexture(TextureAtlas atlas, Function<ResourceLocation, Boolean> addSprite) {
        TextureStitchEvent.INSTANCE.getHandlers().forEach(
            handler -> handler.run(atlas, addSprite));
    }

    private void handleHudRender(PoseStack poseStack, float tickDelta) {
        HudRenderEvent.INSTANCE.getHandlers().forEach(
            handler -> handler.run(poseStack, tickDelta));
    }

    private void handleRenderScreen(AbstractContainerScreen<?> container, PoseStack poseStack, int mouseX, int mouseY) {
        ScreenRenderEvent.INSTANCE.getHandlers().forEach(
            handler -> handler.run(container, poseStack, mouseX, mouseY));
    }

    private void handleSetupScreen(Screen screen) {
        ScreenSetupEvent.INSTANCE.getHandlers().forEach(
            handler -> handler.run(screen));
    }

    private void handleClientTick(Minecraft client) {
        ClientTickEvent.INSTANCE.getHandlers().forEach(handler -> handler.run(client));
    }

    private void handleKeyPresses(Minecraft client) {
        var handlers = KeyPressEvent.INSTANCE.getHandlers();

        for (var id : registry.getKeyMappings().keySet()) {
            var mapping = registry.getKeyMappings().get(id);
            while (mapping.consumeClick()) {
                handlers.forEach(handler -> handler.run(id));
            }
        }
    }
}
