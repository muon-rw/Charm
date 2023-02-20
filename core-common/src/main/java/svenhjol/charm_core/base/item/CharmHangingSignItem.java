package svenhjol.charm_core.base.item;

import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.level.block.*;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

public class CharmHangingSignItem extends HangingSignItem {
    protected final CharmFeature feature;
    protected final IVariantWoodMaterial variantMaterial;
    protected final Supplier<? extends CeilingHangingSignBlock> signBlock;
    protected final Supplier<? extends WallHangingSignBlock> wallSignBlock;

    public <S extends CeilingHangingSignBlock, W extends WallHangingSignBlock> CharmHangingSignItem(CharmFeature feature, IVariantWoodMaterial material, Supplier<S> signBlock, Supplier<W> wallSignBlock) {
        super(Blocks.OAK_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN,
            new Properties().stacksTo(16));

        this.feature = feature;
        this.variantMaterial = material;
        this.signBlock = signBlock;
        this.wallSignBlock = wallSignBlock;
    }

    public Supplier<? extends CeilingHangingSignBlock> getSignBlock() {
        return signBlock;
    }

    public Supplier<? extends WallHangingSignBlock> getWallSignBlock() {
        return wallSignBlock;
    }
}
