package svenhjol.charm.feature.woodcutters;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeHolder;
import svenhjol.charmony.feature.woodcutting.WoodcuttingRecipe;

import java.util.List;

/**
 * Much copypasta from {@link net.minecraft.client.gui.screens.inventory.StonecutterScreen}.
 * TODO: might need to recopy, looks like things have changed a bit.
 */
public class WoodcutterScreen extends AbstractContainerScreen<WoodcutterMenu> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/stonecutter.png");
   private float scrollAmount;
   private boolean mouseClicked;
   private int scrollOffset;
   private boolean canCraft;

   public WoodcutterScreen(WoodcutterMenu handler, Inventory inventory, Component title) {
      super(handler, inventory, title);
      handler.setContentsChangedListener(this::onInventoryChange);
      --this.titleLabelY;
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      super.render(guiGraphics, mouseX, mouseY, delta);
      this.renderTooltip(guiGraphics, mouseX, mouseY);
   }

   protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
      var pose = guiGraphics.pose();
      this.renderBackground(guiGraphics, mouseX, mouseY, delta);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, TEXTURE);
      int i = this.getX();
      int j = this.getY();
      guiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
      int k = (int)(41.0F * this.scrollAmount);
      guiGraphics.blit(TEXTURE, i + 119, j + 15 + k, 176 + (this.shouldScroll() ? 0 : 12), 0, 12, 15);
      int l = this.getX() + 52;
      int m = this.getY() + 14;
      int n = this.scrollOffset + 12;
      this.renderRecipeBackground(guiGraphics, mouseX, mouseY, l, m, n);
      this.renderRecipeIcons(guiGraphics, l, m, n);
   }

   protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
      super.renderTooltip(guiGraphics, x, y);
      if (this.minecraft == null || this.minecraft.level == null) return;
      if (this.canCraft) {
         int i = this.getX() + 52;
         int j = this.getY() + 14;
         int k = this.scrollOffset + 12;
         List<RecipeHolder<WoodcuttingRecipe>> list = (this.menu).getRecipes();

         for (int l = this.scrollOffset; l < k && l < (this.menu).getAvailableRecipeCount(); ++l) {
            int m = l - this.scrollOffset;
            int n = i + m % 4 * 16;
            int o = j + m / 4 * 18 + 2;
            if (x >= n && x < n + 16 && y >= o && y < o + 18) {
               guiGraphics.renderTooltip(this.font, list.get(l)
                   .value()
                   .getResultItem(this.minecraft.level.registryAccess()), x, y);
            }
         }
      }
   }

   private void renderRecipeBackground(GuiGraphics guiGraphics, int i, int j, int k, int l, int m) {
      for(int n = this.scrollOffset; n < m && n < (this.menu).getAvailableRecipeCount(); ++n) {
         int o = n - this.scrollOffset;
         int p = k + o % 4 * 16;
         int q = o / 4;
         int r = l + q * 18 + 2;
         int s = this.imageHeight;
         if (n == (this.menu).getSelectedRecipe()) {
            s += 18;
         } else if (i >= p && j >= r && i < p + 16 && j < r + 18) {
            s += 36;
         }

         guiGraphics.blit(TEXTURE, p, r - 1, 0, s, 16, 18);
      }

   }

   private void renderRecipeIcons(GuiGraphics guiGraphics, int x, int y, int scrollOffset) {
      var list = this.menu.getRecipes();
      if (this.minecraft == null || this.minecraft.level == null) return;

      for (var i = this.scrollOffset; i < scrollOffset && i < (this.menu).getAvailableRecipeCount(); ++i) {
         int j = i - this.scrollOffset;
         int k = x + j % 4 * 16;
         int l = j / 4;
         int m = y + l * 18 + 2;
         guiGraphics.renderItem(list.get(i).value().getResultItem(this.minecraft.level.registryAccess()), k, m);
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.mouseClicked = false;
      if (this.canCraft) {
         int i = this.getX() + 52;
         int j = this.getY() + 14;
         int k = this.scrollOffset + 12;

         for(int l = this.scrollOffset; l < k; ++l) {
            int m = l - this.scrollOffset;
            double d = mouseX - (double)(i + m % 4 * 16);
            double e = mouseY - (double)(j + m / 4 * 18);
            if (d >= 0.0D && e >= 0.0D && d < 16.0D && e < 18.0D && (this.menu).clickMenuButton(this.minecraft.player, l)) {
               Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
               this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
               return true;
            }
         }

         i = this.getX() + 119;
         j = this.getY() + 9;
         if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
            this.mouseClicked = true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.mouseClicked && this.shouldScroll()) {
         int i = this.getY() + 14;
         int j = i + 54;
         this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
         this.scrollAmount = Mth.clamp(this.scrollAmount, 0.0F, 1.0F);
         this.scrollOffset = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5D) * 4;
         return true;
      } else {
         return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
      if (this.shouldScroll()) {
         int i = this.getMaxScroll();
         this.scrollAmount = (float)((double)this.scrollAmount - amount / (double)i);
         this.scrollAmount = Mth.clamp(this.scrollAmount, 0.0F, 1.0F);
         this.scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5D) * 4;
      }

      return true;
   }

   private int getX() {
      return leftPos;
   }

   private int getY() {
      return topPos;
   }

   private boolean shouldScroll() {
      return this.canCraft && (this.menu).getAvailableRecipeCount() > 12;
   }

   protected int getMaxScroll() {
      return ((this.menu).getAvailableRecipeCount() + 4 - 1) / 4 - 3;
   }

   private void onInventoryChange() {
      this.canCraft = (this.menu).canCraft();
      if (!this.canCraft) {
         this.scrollAmount = 0.0F;
         this.scrollOffset = 0;
      }
   }
}
