package svenhjol.charm.feature.extractable_enchantments;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.ArrayList;
import java.util.List;

@ClientFeature
public class ExtractableEnchantmentsClient extends CharmFeature {
    @Override
    public void register() {
        addDependencyCheck(m -> Charm.LOADER.isEnabled(ExtractableEnchantments.class));
    }

    private static ExtractableEnchantments getParent() {
        return Charm.LOADER.get(ExtractableEnchantments.class).orElseThrow();
    }

    public static void updateGrindstoneCost(GrindstoneScreen screen, Player player, PoseStack matrices, Font textRenderer, int width) {
        var parent = getParent();
        var menu = screen.getMenu();

        // Add all slot stacks to list for checking.
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(menu.getSlot(0).getItem());
        stacks.add(menu.getSlot(1).getItem());

        // If it's a disenchant operation.
        if (parent.shouldExtract(stacks)) {

            // Get the stack to disenchant.
            var enchanted = parent.getEnchantedItemFromStacks(stacks);
            if (enchanted.isEmpty()) return;

            // Get the stack cost and render it.
            var cost = parent.getCost(enchanted.get());

            var color = 8453920;
            var string = I18n.get("container.repair.cost", cost);

            if (!parent.hasEnoughXp(player, cost)) {
                color = 16736352;
            }

            var k = width - 8 - textRenderer.width(string) - 2;
            GuiComponent.fill(matrices, k - 2, 67, width - 8, 79, 1325400064);
            textRenderer.drawShadow(matrices, string, (float)k, 69.0F, color);
        }
    }
}
