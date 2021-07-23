package svenhjol.charm.mixin.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.AddEntityEvent;

@Mixin(PersistentEntitySectionManager.class)
public class AddEntityCallbackManagerMixin<T extends EntityAccess> {
    /**
     * Fires the {@link AddEntityEvent} event when an entity
     * is registered and before tracking is applied.
     */
    @Inject(
        method = "addEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/SectionPos;asLong(Lnet/minecraft/core/BlockPos;)J"
        )
    )
    private void hookLoadEntity(T entity, boolean existing, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Entity) {
            AddEntityEvent.EVENT.invoker().interact((Entity)entity);
        }
    }
}