package svenhjol.charm_api.iface;

import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

public interface IProvidesHarvestables {
    List<Supplier<BlockState>> getHarvestableBlocks();
}
