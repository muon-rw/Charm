package svenhjol.charm.feature.player_pressure_plates;

import net.minecraft.world.level.ItemLike;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.api.iface.IWandererTrade;
import svenhjol.charmony.api.iface.IWandererTradeProvider;

import java.util.List;
import java.util.function.Supplier;

public class PlayerPressurePlates extends CommonFeature implements IWandererTradeProvider {
    static final String ID = "player_pressure_plate";
    static Supplier<PlayerPressurePlateBlock> block;
    static Supplier<PlayerPressurePlateBlock.BlockItem> blockItem;

    @Override
    public String description() {
        return "Player-only pressure plates crafted using smooth basalt.";
    }

    @Override
    public void register() {
        var registry = mod().registry();
        block = registry.block(ID, PlayerPressurePlateBlock::new);
        blockItem = registry.item(ID, () -> new PlayerPressurePlateBlock.BlockItem(block));
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return block.get();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public int getCost() {
                return 6;
            }
        });
    }
}
