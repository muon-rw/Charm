package svenhjol.charm.mixin.accessor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Gui.class)
public interface GuiAccessor {
    @Accessor("screenWidth")
    int getScreenWidth();

    @Invoker("drawBackdrop")
    void invokeDrawBackdrop(PoseStack poseStack, Font font, int x, int y, int alpha);
}
