package svenhjol.charm.feature.kilns;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "A functional block that speeds up cooking of clay, glass, bricks and terracotta.")
public class Kilns extends CharmFeature {
    private static final String BLOCK_ID = "kiln";
    public static Supplier<KilnBlock> BLOCK;
    public static Supplier<BlockItem> BLOCK_ITEM;
    public static Supplier<MenuType<KilnMenu>> MENU;
    public static Supplier<BlockEntityType<KilnBlockEntity>> BLOCK_ENTITY;
    public static Supplier<RecipeBookType> RECIPE_BOOK_TYPE;
    public static Supplier<SoundEvent> BAKE_SOUND;

    @Override
    public void preRegister() {
        Charm.REGISTRY.recipeBookTypeEnum("kiln");
    }

    @Override
    public void register() {
        BLOCK = Charm.REGISTRY.block(BLOCK_ID, () -> new KilnBlock(this));
        BLOCK_ITEM = Charm.REGISTRY.item(BLOCK_ID, () -> new KilnBlock.BlockItem(this, BLOCK));
        BLOCK_ENTITY = Charm.REGISTRY.blockEntity(BLOCK_ID, () -> KilnBlockEntity::new, List.of(BLOCK));

        RECIPE_BOOK_TYPE = Charm.REGISTRY.recipeBookType(BLOCK_ID);
        MENU = Charm.REGISTRY.menuType(BLOCK_ID, () -> new MenuType<>(KilnMenu::new));

        BAKE_SOUND = Charm.REGISTRY.soundEvent("kiln_bake");
    }
}
