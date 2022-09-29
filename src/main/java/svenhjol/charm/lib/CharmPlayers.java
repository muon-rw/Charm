package svenhjol.charm.lib;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class CharmPlayers {
    /**
     * Returns list of players in given range of pos.
     */
    public static List<Player> getInRange(Level level, BlockPos pos, double range) {
        return level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(range));
    }
}
