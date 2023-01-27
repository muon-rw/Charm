package svenhjol.charm.feature.block_of_gunpowder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Configurable;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Gunpowder storage block. Dissolves when touching lava.")
public class BlockOfGunpowder extends CharmFeature {
    private static final String ID = "gunpowder_block";
    static Supplier<Block> BLOCK;
    static Supplier<Item> BLOCK_ITEM;
    static Supplier<SoundEvent> DISSOLVE_SOUND;

    @Configurable(name = "Dissolve in lava", description = "If true, gunpowder blocks will dissolve when touching lava.")
    public static boolean dissolve = true;

    @Override
    public void register() {
        BLOCK = Charm.REGISTRY.block(ID, GunpowderBlock::new);
        BLOCK_ITEM = Charm.REGISTRY.item(ID, GunpowderBlock.BlockItem::new);
        DISSOLVE_SOUND = Charm.REGISTRY.soundEvent("gunpowder_dissolve");
    }
}
