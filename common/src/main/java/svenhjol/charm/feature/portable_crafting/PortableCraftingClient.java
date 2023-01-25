package svenhjol.charm.feature.portable_crafting;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.mixin.accessor.AbstractContainerScreenAccessor;
import svenhjol.charm_api.event.KeyPressEvent;
import svenhjol.charm_api.event.ScreenRenderEvent;
import svenhjol.charm_api.event.ScreenSetupEvent;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.helper.ScreenHelper;
import svenhjol.charm_core.init.CoreResources;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@ClientFeature
public class PortableCraftingClient extends CharmFeature {
    private static Supplier<String> OPEN_PORTABLE_CRAFTING_KEY;
    private static final int CHECK_TICKS_FAST = 5;
    private static final int LEFT = 76;
    private ImageButton craftingButton;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(PortableCrafting.class));
    }

    @Override
    public void register() {
        OPEN_PORTABLE_CRAFTING_KEY = CharmClient.REGISTRY.key("open_portable_crafting",
            () -> new KeyMapping("key.charm.open_portable_crafting", GLFW.GLFW_KEY_V, "key.categories.inventory"));
    }

    @Override
    public void runWhenEnabled() {
        KeyPressEvent.INSTANCE.handle(this::handleKeyPress);
        ScreenSetupEvent.INSTANCE.handle(this::handleScreenSetup);
        ScreenRenderEvent.INSTANCE.handle(this::handleScreenRender);
    }

    private void handleScreenSetup(Screen screen) {
        var client = Minecraft.getInstance();

        if (client.player == null) return;
        if (!(screen instanceof InventoryScreen inventoryScreen)) return;

        int leftPos = ((AbstractContainerScreenAccessor)inventoryScreen).getLeftPos();
        int midY = inventoryScreen.height / 2;

        this.craftingButton = new ImageButton(leftPos + LEFT, midY - 66, 20, 18, 0, 0, 19, CoreResources.INVENTORY_BUTTONS,
            click -> openCraftingTable());

        this.craftingButton.visible = PortableCrafting.hasCraftingTable(client.player);
        ScreenHelper.addRenderableWidget(inventoryScreen, this.craftingButton);
    }

    private void handleScreenRender(AbstractContainerScreen<?> screen, PoseStack poseStack, int mouseX, int mouseY) {
        var client = Minecraft.getInstance();

        if (!(client.screen instanceof InventoryScreen)) return;
        if (craftingButton == null || client.player == null) return;

        if (client.player.level.getGameTime() % CHECK_TICKS_FAST == 0) {
            craftingButton.visible = PortableCrafting.hasCraftingTable(client.player);
        }

        // Re-render when recipe is opened/closed.
        var x = ((AbstractContainerScreenAccessor)screen).getLeftPos();
        craftingButton.setPosition(x + LEFT, craftingButton.getY());
    }

    private void handleKeyPress(String id) {
        if (id.equals(OPEN_PORTABLE_CRAFTING_KEY.get())) {
            openCraftingTable();
        }
    }

    private void openCraftingTable() {
        PortableCraftingNetwork.OpenCrafting.send();
    }
}
