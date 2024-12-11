package svenhjol.charm.mixin.feature.totem_of_preserving;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Shadow
    @Final
    private Level level;

    @Shadow @Final private ObjectArrayList<BlockPos> toBlow;

    @Inject(
            method = "finalizeExplosion",
            at = @At("HEAD")
    )
    private void hookFinalizeExplosion(boolean bl, CallbackInfo ci) {
        var dimension = this.level.dimension().location();
        var feature = Resolve.feature(TotemOfPreserving.class);
        if (feature.handlers.protectedPositions.containsKey(dimension)) {
            for (BlockPos pos : feature.handlers.protectedPositions.get(dimension)) {
                if (this.level.getBlockState(pos).getBlock() instanceof net.minecraft.world.level.block.TntBlock) {
                    continue;
                }
                this.toBlow.remove(pos);
            }
        }
    }
}
