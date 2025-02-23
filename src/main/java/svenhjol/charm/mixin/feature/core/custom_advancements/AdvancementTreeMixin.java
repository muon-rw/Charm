package svenhjol.charm.mixin.feature.core.custom_advancements;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementTree;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.core.custom_advancements.CustomAdvancements;

@Mixin(AdvancementTree.class)
public class AdvancementTreeMixin {
    /**
     * Filter out Charm's advancements early.
     */
    @Inject(
        method = "tryInsert",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookTryInsert(AdvancementHolder holder, CallbackInfoReturnable<Boolean> cir) {
        if (Resolve.feature(CustomAdvancements.class).handlers.shouldRemove(holder.id())) {
            cir.setReturnValue(true); // Return early, don't register the filtered advancement.
        }
    }
}
