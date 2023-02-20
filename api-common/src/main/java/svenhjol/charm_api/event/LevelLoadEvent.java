package svenhjol.charm_api.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class LevelLoadEvent extends CharmEvent<LevelLoadEvent.Handler> {
    public static final LevelLoadEvent INSTANCE = new LevelLoadEvent();

    private LevelLoadEvent() {}

    public void invoke(MinecraftServer server, ServerLevel level) {
        for (Handler handler : getHandlers()) {
            handler.run(server, level);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(MinecraftServer server, ServerLevel level);
    }
}
