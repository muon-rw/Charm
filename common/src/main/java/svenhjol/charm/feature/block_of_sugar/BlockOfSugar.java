package svenhjol.charm.feature.block_of_sugar;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Configurable;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Sugar storage block. Dissolves when touching water.")
public class BlockOfSugar extends CharmFeature {
    private static final String ID = "sugar_block";
    static Supplier<Block> BLOCK;
    static Supplier<Item> BLOCK_ITEM;
    static Supplier<SoundEvent> DISSOLVE_SOUND;

    @Configurable(name = "Dissolve in water", description = "If true, sugar blocks will dissolve when touching water.")
    public static boolean dissolve = true;

    @Override
    public void register() {
        BLOCK = Charm.REGISTRY.block(ID, SugarBlock::new);
        BLOCK_ITEM = Charm.REGISTRY.item(ID, SugarBlock.BlockItem::new);
        DISSOLVE_SOUND = Charm.REGISTRY.soundEvent("sugar_dissolve");
    }
}
