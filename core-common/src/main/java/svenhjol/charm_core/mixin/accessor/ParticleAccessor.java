package svenhjol.charm_core.mixin.accessor;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor("friction")
    void setFriction(float friction);

    @Accessor("speedUpWhenYMotionIsBlocked")
    void setSpeedUpWhenYMotionIsBlocked(boolean flag);

    @Invoker("setAlpha")
    void invokeSetAlpha(float alpha);
}
