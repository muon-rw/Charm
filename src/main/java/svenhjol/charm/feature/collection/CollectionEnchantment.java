package svenhjol.charm.feature.collection;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import svenhjol.charm.Charm;
import svenhjol.charmony.base.CharmonyEnchantment;
import svenhjol.charmony.base.Mods;

public class CollectionEnchantment extends CharmonyEnchantment {
    public CollectionEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.DIGGER, new EquipmentSlot[] { EquipmentSlot.MAINHAND});
    }
    
    @Override
    public int getMinCost(int level) {
        return 15;
    }

    @Override
    public boolean isEnabled() {
        return Mods.common(Charm.ID).loader().isEnabled(Collection.class);
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return isEnabled() && (stack.getItem() instanceof ShearsItem || super.canEnchant(stack));
    }
}