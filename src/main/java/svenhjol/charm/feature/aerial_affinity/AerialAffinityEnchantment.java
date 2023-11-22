package svenhjol.charm.feature.aerial_affinity;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import svenhjol.charm.Charm;
import svenhjol.charmony.base.CharmonyEnchantment;
import svenhjol.charmony.base.Mods;

public class AerialAffinityEnchantment extends CharmonyEnchantment {
    public AerialAffinityEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[] { EquipmentSlot.FEET });
    }
    
    @Override
    public int getMinCost(int level) {
        return 1;
    }
    
    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 40;
    }

    @Override
    public boolean isEnabled() {
        return Mods.common(Charm.ID).loader().isEnabled(AerialAffinity.class);
    }
}