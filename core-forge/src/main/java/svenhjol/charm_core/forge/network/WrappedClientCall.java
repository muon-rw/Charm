package svenhjol.charm_core.forge.network;

import net.minecraft.client.Minecraft;
import svenhjol.charm_core.iface.IPacketHandler;
import svenhjol.charm_core.iface.IPacketRequest;

import java.util.function.Supplier;

/**
 * Required so that we can get an instance of the localplayer to pass to the client packet handler.
 * This can't be included in ServerToClientPacket because client Minecraft class can't load on the server side..
 * @see ServerToClientPacket
 */
public class WrappedClientCall {
    public static <T extends IPacketRequest, U extends IPacketHandler<T>> void handle(Supplier<U> handler, T self) {
        handler.get().handle(self, Minecraft.getInstance().player);
    }
}
