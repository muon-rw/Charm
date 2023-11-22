package svenhjol.charm.mixin.no_experimental_screen;

import com.mojang.serialization.Dynamic;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {
    @Shadow protected abstract void loadLevel(LevelStorageSource.LevelStorageAccess levelStorageAccess, Dynamic<?> dynamic, boolean bl, boolean bl2, Runnable runnable);

    @Unique
    private boolean skippedDialog;

    @Redirect(
        method = "loadLevel*",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows;askForBackup(Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;ZLjava/lang/Runnable;Ljava/lang/Runnable;)V"
        )
    )
    private void hookDisplayDialog(WorldOpenFlows instance, LevelStorageSource.LevelStorageAccess levelStorageAccess, boolean bl, Runnable runnable, Runnable runnable2) {
        skippedDialog = true;
    }

    @Inject(
        method = "loadLevel*",
        at = @At("RETURN")
    )
    private void hookForceLoadLevel(LevelStorageSource.LevelStorageAccess levelStorageAccess, Dynamic<?> dynamic, boolean bl, boolean bl2, Runnable runnable, CallbackInfo ci) {
        if (skippedDialog) {
            skippedDialog = false;
            loadLevel(levelStorageAccess, dynamic, bl, false, runnable);
        }
    }
}
