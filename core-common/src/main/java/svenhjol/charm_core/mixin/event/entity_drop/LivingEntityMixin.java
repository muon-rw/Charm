package svenhjol.charm_core.mixin.event.entity_drop;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm_api.event.EntityKilledDropEvent;

import javax.annotation.Nullable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow @Nullable
    public abstract LivingEntity getLastHurtByMob();
    
    /**
     * Fires the {@link EntityKilledDropEvent} event.
     */
    @Inject(
        method = "dropAllDeathLoot",
        at = @At("TAIL")
    )
    private void hookDrop(DamageSource source, CallbackInfo ci) {
        int lootingLevel = 0;
        var entity = (LivingEntity)(Object)this;
        var mob = getLastHurtByMob();
        
        if (mob != null) {
            lootingLevel = EnchantmentHelper.getMobLooting(mob);
        }
        
        EntityKilledDropEvent.INSTANCE.invoke(entity, source, lootingLevel);
    }
}