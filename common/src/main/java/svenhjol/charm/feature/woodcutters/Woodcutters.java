package svenhjol.charm.feature.woodcutters;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, priority = 1, description = "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.")
public class Woodcutters extends CharmFeature {
    public static final String BLOCK_ID = "woodcutter";
    public static Supplier<WoodcutterBlock> BLOCK;
    public static Supplier<WoodcutterBlock.BlockItem> BLOCK_ITEM;
    public static Supplier<MenuType<WoodcutterMenu>> MENU;
    public static Supplier<PoiType> POI_TYPE;
    public static Supplier<SoundEvent> USE_SOUND;
    public static Supplier<RecipeBookType> RECIPE_BOOK_TYPE;

    @Override
    public void preRegister() {
        Charm.REGISTRY.recipeBookTypeEnum("woodcutter");
    }

    @Override
    public void register() {
        BLOCK = Charm.REGISTRY.block(BLOCK_ID,
            () -> new WoodcutterBlock(this));
        BLOCK_ITEM = Charm.REGISTRY.item(BLOCK_ID,
            () -> new WoodcutterBlock.BlockItem(this, BLOCK));

        POI_TYPE = Charm.REGISTRY.pointOfInterestType(BLOCK_ID,
            () -> new PoiType(ImmutableSet.copyOf(BLOCK.get().getStateDefinition().getPossibleStates()), 1, 1));

        RECIPE_BOOK_TYPE = Charm.REGISTRY.recipeBookType(BLOCK_ID);
        MENU = Charm.REGISTRY.menuType(BLOCK_ID,
            () -> new MenuType<>(WoodcutterMenu::new));

        USE_SOUND = Charm.REGISTRY.soundEvent("woodcutter_use");
    }
}
