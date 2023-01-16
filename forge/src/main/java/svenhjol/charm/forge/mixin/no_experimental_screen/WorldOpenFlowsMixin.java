package svenhjol.charm.forge.mixin.no_experimental_screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {
    private boolean skippedDialog;

    @Inject(
        method = "doLoadLevel(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;ZZ)V",
        at = @At("HEAD")
    )
    private void hookDisplayDialog(Screen screen, String string, boolean bl, boolean bl2, CallbackInfo ci) {
        skippedDialog = true;
    }

    @ModifyArg(
        method = "doLoadLevel(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;ZZZ)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/PrimaryLevelData;withConfirmedWarning(Z)Lnet/minecraft/world/level/storage/PrimaryLevelData;"
        )
    )
    private boolean hookWithConfirmedWarning(boolean par1) {
        return skippedDialog;
    }
}
