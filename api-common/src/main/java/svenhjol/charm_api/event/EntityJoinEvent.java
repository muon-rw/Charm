package svenhjol.charm_api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityJoinEvent extends CharmEvent<EntityJoinEvent.Handler> {
    public static final EntityJoinEvent INSTANCE = new EntityJoinEvent();

    private EntityJoinEvent() {}

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
