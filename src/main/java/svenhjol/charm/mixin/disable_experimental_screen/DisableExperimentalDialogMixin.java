package svenhjol.charm.mixin.disable_experimental_screen;

import net.minecraft.client.Minecraft;
import net.minecraft.server.WorldStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(Minecraft.class)
public abstract class DisableExperimentalDialogMixin {
    @Shadow protected abstract void doLoadLevel(String string, Function<LevelStorageSource.LevelStorageAccess, WorldStem.DataPackConfigSupplier> function, Function<LevelStorageSource.LevelStorageAccess, WorldStem.WorldDataSupplier> function2, boolean bl, Minecraft.ExperimentalDialogType experimentalDialogType);

    private boolean skippedDialog;

    @Redirect(
        method = "doLoadLevel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;displayExperimentalConfirmationDialog(Lnet/minecraft/client/Minecraft$ExperimentalDialogType;Ljava/lang/String;ZLjava/lang/Runnable;)V"
        )
    )
    private void hookDisplayDialog(Minecraft instance, Minecraft.ExperimentalDialogType experimentalDialogType, String string, boolean bl, Runnable runnable) {
        skippedDialog = true;
    }

    @Inject(
        method = "doLoadLevel",
        at = @At("RETURN")
    )
    private void hookForceLoadLevel(String string, Function<LevelStorageSource.LevelStorageAccess, WorldStem.DataPackConfigSupplier> function, Function<LevelStorageSource.LevelStorageAccess, WorldStem.WorldDataSupplier> function2, boolean bl, Minecraft.ExperimentalDialogType experimentalDialogType, CallbackInfo ci) {
        if (skippedDialog) {
            skippedDialog = false;
            doLoadLevel(string, function, function2, bl, Minecraft.ExperimentalDialogType.NONE);
        }
    }
}
