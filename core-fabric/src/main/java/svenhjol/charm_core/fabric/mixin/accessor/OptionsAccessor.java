package svenhjol.charm_core.fabric.mixin.accessor;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Options.class)
public interface OptionsAccessor {
    @Accessor("discreteMouseScroll")
    OptionInstance<Boolean> getDiscreteMouseScroll();

    @Accessor("mouseWheelSensitivity")
    OptionInstance<Double> getMouseWheelSensitivity();
}
