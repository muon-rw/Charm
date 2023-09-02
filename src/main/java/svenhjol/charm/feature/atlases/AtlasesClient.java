package svenhjol.charm.feature.atlases;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.mixin.atlases.CartographyTableScreenMixin;
import svenhjol.charm_api.event.HeldItemRenderEvent;
import svenhjol.charm_api.event.KeyPressEvent;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@ClientFeature
public class AtlasesClient extends CharmFeature {
    public static final RenderType ATLAS_BACKGROUND =
        RenderType.text(Charm.instance().makeId("textures/map/atlas.png"));
    public static final RenderType MAP_BACKGROUND =
        RenderType.text(new ResourceLocation("textures/map/map_background.png"));
    static final ResourceLocation CONTAINER_BACKGROUND =
        Charm.instance().makeId("textures/gui/atlas.png");
    private AtlasRenderer renderer;
    private static int swappedSlot = -1;

    public static Supplier<String> OPEN_ATLAS_KEY;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(Atlases.class));
    }

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();
        registry.menuScreen(Atlases.MENU_TYPE, () -> AtlasScreen::new);

        OPEN_ATLAS_KEY = registry.key("open_atlas",
            () -> new KeyMapping("key.charm.open_atlas", GLFW.GLFW_KEY_R, "key.categories.inventory"));

        if (isEnabled()) {
            registry.itemTab(
                Atlases.ITEM,
                CreativeModeTabs.TOOLS_AND_UTILITIES,
                Items.MAP
            );
        }
    }

    @Override
    public void runWhenEnabled() {
        KeyPressEvent.INSTANCE.handle(this::handleKeyPress);
        HeldItemRenderEvent.INSTANCE.handle(this::handleRenderHeldItem);
    }

    private void handleKeyPress(String id) {
        if (id.equals(OPEN_ATLAS_KEY.get())) {
            AtlasesNetwork.SwapAtlasSlot.send(swappedSlot);
        }
    }

    private InteractionResult handleRenderHeldItem(float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack itemStack, float equipProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        if (itemStack.getItem() == Atlases.ITEM.get()) {
            if (renderer == null) {
                renderer = new AtlasRenderer();
            }
            renderer.renderAtlas(poseStack, multiBufferSource, light, hand, equipProgress, swingProgress, itemStack);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public static void handleUpdateInventory(AtlasesNetwork.UpdateInventory request, Player player) {
        var slot = request.getSlot();
        ItemStack atlas = player.getInventory().getItem(slot);
        AtlasInventory.get(player.level(), atlas).reload(atlas);
    }

    /**
     * Callback from {@link CartographyTableScreenMixin} to check
     * if the cartography table contains a map or an atlas.
     * @param screen The cartography table screen.
     * @return True if the cartography table contains a map or an atlas.
     */
    public static boolean shouldDrawAtlasCopy(CartographyTableScreen screen) {
        return screen.getMenu().getSlot(0).getItem().getItem() == Atlases.ITEM.get()
            && screen.getMenu().getSlot(1).getItem().getItem() == Items.MAP;
    }

    public static void handleSwappedSlot(AtlasesNetwork.SwappedAtlasSlot packet, Player player) {
        swappedSlot = packet.getSlot();
    }
}
