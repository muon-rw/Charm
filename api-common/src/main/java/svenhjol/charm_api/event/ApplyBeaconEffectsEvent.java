package svenhjol.charm_api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;

public class ApplyBeaconEffectsEvent extends CharmEvent<ApplyBeaconEffectsEvent.Handler> {
    public static final ApplyBeaconEffectsEvent INSTANCE = new ApplyBeaconEffectsEvent();

    private ApplyBeaconEffectsEvent() {}

    public void invoke(Level level, BlockPos pos, int beaconLevel, MobEffect primary, MobEffect secondary) {
        for (Handler handler : getHandlers()) {
            handler.run(level, pos, beaconLevel, primary, secondary);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(Level level, BlockPos pos, int beaconLevel, MobEffect primary, MobEffect secondary);
    }
}
