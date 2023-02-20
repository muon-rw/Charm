package svenhjol.charm_api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ClientEntityLeaveEvent extends CharmEvent<ClientEntityLeaveEvent.Handler> {
    public static final ClientEntityLeaveEvent INSTANCE = new ClientEntityLeaveEvent();

    private ClientEntityLeaveEvent() {}

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
