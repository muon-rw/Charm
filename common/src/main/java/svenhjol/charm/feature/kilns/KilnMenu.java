package svenhjol.charm.feature.kilns;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import svenhjol.charm.feature.firing.Firing;

public class KilnMenu extends AbstractFurnaceMenu {
    public KilnMenu(int syncId, Inventory playerInventory) {
        super(Kilns.MENU.get(), Firing.RECIPE_TYPE.get(), RecipeBookType.valueOf("KILN"), syncId, playerInventory);
    }

    public KilnMenu(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(Kilns.MENU.get(), Firing.RECIPE_TYPE.get(), RecipeBookType.valueOf("KILN"), syncId, playerInventory, inventory, propertyDelegate);
    }
}
