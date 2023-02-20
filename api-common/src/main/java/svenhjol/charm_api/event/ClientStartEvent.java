package svenhjol.charm_api.event;

import net.minecraft.client.Minecraft;

public class ClientStartEvent extends CharmEvent<ClientStartEvent.Handler> {
    public static final ClientStartEvent INSTANCE = new ClientStartEvent();

    private ClientStartEvent() {}

    public void invoke(Minecraft client) {
        for (Handler handler : getHandlers()) {
            handler.run(client);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(Minecraft client);
    }
}
