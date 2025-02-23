package svenhjol.charm.mixin.feature.silence;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilUserApiService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.silence.Silence;

@Mixin(YggdrasilUserApiService.class)
public class YggdrasilUserApiServiceMixin {
    @Inject(
        method = "fetchProperties",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private void hookFetchProperties(CallbackInfoReturnable<UserApiService.UserProperties> cir) {
        if (Silence.disableDevEnvironmentConnections()) {
            cir.setReturnValue(UserApiService.OFFLINE_PROPERTIES);
        }
    }
}
