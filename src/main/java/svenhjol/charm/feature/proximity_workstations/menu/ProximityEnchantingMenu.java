package svenhjol.charm.feature.proximity_workstations.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.level.block.Blocks;

public class ProximityEnchantingMenu extends EnchantmentMenu {
    private final BlockPos pos;

    public ProximityEnchantingMenu(int syncId, Inventory playerInventory, ContainerLevelAccess access, BlockPos pos) {
        super(syncId, playerInventory, access);
        this.pos = pos;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.level().getBlockState(pos).is(Blocks.ENCHANTING_TABLE);
    }
}
