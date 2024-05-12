package svenhjol.charm.mixin.feature.silence_messages;

import com.mojang.realmsclient.RealmsAvailability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.silence_messages.common.Handlers;

import java.util.concurrent.CompletableFuture;

@Mixin(RealmsAvailability.class)
public class RealmsAvailabilityMixin {
    @Inject(
        method = "check",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookCheck(CallbackInfoReturnable<CompletableFuture<RealmsAvailability.Result>> cir) {
        if (Handlers.disableDevEnvironmentConnections()) {
            cir.setReturnValue(CompletableFuture.supplyAsync(() -> new RealmsAvailability.Result(RealmsAvailability.Type.INCOMPATIBLE_CLIENT)));
        }
    }
}