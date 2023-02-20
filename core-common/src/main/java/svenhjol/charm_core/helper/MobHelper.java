package svenhjol.charm_core.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class MobHelper {
    @Nullable
    public static <T extends Mob> T spawn(EntityType<T> type, ServerLevel level, BlockPos pos, MobSpawnType reason, Consumer<T> beforeAddToLevel) {
        T mob = type.create(level, null, null, pos, reason, false, false);
        if (mob != null) {
            beforeAddToLevel.accept(mob);
            level.addFreshEntity(mob);
        }

        return mob;
    }
}
