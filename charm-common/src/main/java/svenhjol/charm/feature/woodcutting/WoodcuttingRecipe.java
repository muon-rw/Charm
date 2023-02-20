package svenhjol.charm.feature.woodcutting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;

public class WoodcuttingRecipe extends SingleItemRecipe {
   public Item icon = Items.AIR;

   public WoodcuttingRecipe(ResourceLocation id, String group, Ingredient input, ItemStack output) {
      super(Woodcutting.RECIPE_TYPE.get(), Woodcutting.RECIPE_SERIALIZER.get(), id, group, input, output);
   }

   public Ingredient getInput() {
      return this.ingredient;
   }

   public ItemStack getResultItem() {
      return this.result.copy();
   }

   public boolean matches(Container inv, Level level) {
      return this.ingredient.test(inv.getItem(0));
   }

   public ItemStack getRecipeKindIcon() {
      return new ItemStack(icon);
   }
}
