package svenhjol.charm.integration.rei;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charmony.helper.TextHelper;

import java.util.List;

/**
 * See me.shedaniel.rei.plugin.client.categories.DefaultStoneCuttingCategory
 * for reference implementation.
 */
@Environment(EnvType.CLIENT)
public class WoodcuttingCategory implements DisplayCategory<WoodcuttingDisplay> {
    @Override
    public CategoryIdentifier<? extends WoodcuttingDisplay> getCategoryIdentifier() {
        return ICharmReiPlugin.WOODCUTTING;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Woodcutters.block.get());
    }

    @Override
    public Component getTitle() {
        return TextHelper.translatable("category.rei.charm.woodcutting");
    }

    @Override
    public List<Widget> setupDisplay(WoodcuttingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
            .entries(display.getOutputEntries().get(0))
            .disableBackground()
            .markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5))
            .entries(display.getInputEntries().get(0)).markInput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }
}