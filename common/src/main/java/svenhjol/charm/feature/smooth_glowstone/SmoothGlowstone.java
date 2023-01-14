package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.BlockItem;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Smooth glowstone.")
public class SmoothGlowstone extends CharmFeature {
    public static final String ID = "smooth_glowstone";
    public static Supplier<SmoothGlowstoneBlock> BLOCK;
    public static Supplier<BlockItem> BLOCK_ITEM;

    @Override
    public void register() {
        BLOCK = Charm.REGISTRY.block(ID, () -> new SmoothGlowstoneBlock(this));
        BLOCK_ITEM = Charm.REGISTRY.item(ID, () -> new SmoothGlowstoneBlock.BlockItem(this, BLOCK));
    }
}
