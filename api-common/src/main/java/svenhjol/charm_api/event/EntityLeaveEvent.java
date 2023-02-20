package svenhjol.charm_api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityLeaveEvent extends CharmEvent<EntityLeaveEvent.Handler> {
    public static final EntityLeaveEvent INSTANCE = new EntityLeaveEvent();

    private EntityLeaveEvent() {}

    public void invoke(Entity entity, Level level) {
        for (Handler handler : getHandlers()) {
            handler.run(entity, level);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(Entity entity, Level level);
    }
}
