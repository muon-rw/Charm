package svenhjol.charm_api.iface;

import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public interface IHasEnchantments {
    List<Enchantment> getEnchantments();
}
