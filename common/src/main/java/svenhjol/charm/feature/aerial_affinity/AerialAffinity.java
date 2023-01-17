package svenhjol.charm.feature.aerial_affinity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm_api.event.BlockBreakSpeedEvent;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Aerial Affinity is a boots enchantment that increases mining rate when not on the ground.")
public class AerialAffinity extends CharmFeature {
    private static final String ID = "aerial_affinity";
    private static Supplier<Enchantment> ENCHANTMENT;
    
    @Override
    public void register() {
        ENCHANTMENT = Charm.REGISTRY.enchantment(ID, () -> new AerialAffinityEnchantment(this));
    }
    
    @Override
    public void runWhenEnabled() {
        BlockBreakSpeedEvent.INSTANCE.handle(this::handleBlockBreakSpeed);
    }
    
    private float handleBlockBreakSpeed(Player player, BlockState state, float currentSpeed) {
        if (!player.isOnGround() && EnchantmentHelper.getEnchantmentLevel(ENCHANTMENT.get(), player) > 0) {
            return currentSpeed * 5.0F;
        }
        
        return currentSpeed;
    }
}