package svenhjol.charm.feature.player_pressure_plates;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charm_api.iface.IProvidesWandererTrades;
import svenhjol.charm_api.iface.IWandererTrade;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Player-only pressure plates crafted using gilded blackstone.")
public class PlayerPressurePlates extends CharmFeature implements IProvidesWandererTrades {
    static final String ID = "player_pressure_plate";
    static Supplier<PlayerPressurePlateBlock> BLOCK;
    static Supplier<PlayerPressurePlateBlock.BlockItem> BLOCK_ITEM;

    @Override
    public void register() {
        BLOCK = Charm.REGISTRY.block(ID, () -> new PlayerPressurePlateBlock(this));
        BLOCK_ITEM = Charm.REGISTRY.item(ID, () -> new PlayerPressurePlateBlock.BlockItem(this, BLOCK));
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return BLOCK.get();
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

    @Override
    public List<IWandererTrade> getRareWandererTrades() {
        return List.of();
    }
}
