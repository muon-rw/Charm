package svenhjol.charm_api.event;

import net.minecraft.client.Minecraft;

public class ClientTickEvent extends CharmEvent<ClientTickEvent.Handler> {
    public static final ClientTickEvent INSTANCE = new ClientTickEvent();

    private ClientTickEvent() {}

    @FunctionalInterface
    public interface Handler {
        void run(Minecraft client);
    }
}
