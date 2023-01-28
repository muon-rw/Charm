package svenhjol.charm.feature.redstone_sand;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm_api.iface.IProvidesWandererTrades;
import svenhjol.charm_api.iface.IWandererTrade;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "A block that acts like sand but is powered like a block of redstone.")
public class RedstoneSand extends CharmFeature implements IProvidesWandererTrades {
    private static final String ID = "redstone_sand";
    static Supplier<Block> BLOCK;
    static Supplier<Item> BLOCK_ITEM;

    @Override
    public void register() {
        BLOCK = Charm.REGISTRY.block(ID, RedstoneSandBlock::new);
        BLOCK_ITEM = Charm.REGISTRY.item(ID, RedstoneSandBlock.BlockItem::new);
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return BLOCK_ITEM.get();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public int getCost() {
                return 1;
            }
        });
    }

    @Override
    public List<IWandererTrade> getRareWandererTrades() {
        return null;
    }
}
