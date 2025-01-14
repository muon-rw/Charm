package svenhjol.charm.charmony.common.item;

import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;

import java.util.function.Supplier;

public class CharmHangingSignItem extends HangingSignItem {
    protected final CustomWoodMaterial material;
    protected final Supplier<? extends CeilingHangingSignBlock> hangingBlock;
    protected final Supplier<? extends WallHangingSignBlock> wallSignBlock;

    public <S extends CeilingHangingSignBlock, W extends WallHangingSignBlock> CharmHangingSignItem(CustomWoodMaterial material, Supplier<S> hangingBlock, Supplier<W> wallSignBlock) {
        super(Blocks.OAK_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN,
            new Properties().stacksTo(16));

        this.material = material;
        this.hangingBlock = hangingBlock;
        this.wallSignBlock = wallSignBlock;
    }

    public Supplier<? extends CeilingHangingSignBlock> getHangingBlock() {
        return hangingBlock;
    }

    public Supplier<? extends WallHangingSignBlock> getWallSignBlock() {
        return wallSignBlock;
    }
}
