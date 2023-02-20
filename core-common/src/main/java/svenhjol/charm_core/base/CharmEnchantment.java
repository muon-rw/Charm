package svenhjol.charm_core.base;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class CharmEnchantment extends Enchantment {
    protected final CharmFeature feature;
    
    public CharmEnchantment(CharmFeature feature, Rarity rarity, EnchantmentCategory category, EquipmentSlot[] equipmentSlots) {
        super(rarity, category, equipmentSlots);
        this.feature = feature;
    }
    
    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return feature.isEnabled() && super.canEnchant(itemStack);
    }
    
    @Override
    public boolean isTradeable() {
        return feature.isEnabled() && super.isTradeable();
    }
    
    @Override
    public boolean isDiscoverable() {
        return feature.isEnabled() && super.isDiscoverable();
    }
}