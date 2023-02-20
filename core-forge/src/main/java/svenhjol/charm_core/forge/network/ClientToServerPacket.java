package svenhjol.charm_core.forge.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import svenhjol.charm_core.iface.IPacketHandler;
import svenhjol.charm_core.iface.IPacketRequest;

import java.util.function.Supplier;

public class ClientToServerPacket<T extends IPacketRequest, U extends IPacketHandler<T>> {
    private final T packet;
    private final Supplier<U> handler;

    public ClientToServerPacket(T packet, Supplier<U> handler) {
        this.packet = packet;
        this.handler = handler;
    }

    public void encode(T self, FriendlyByteBuf buf) {
        self.encode(buf);
    }

    public T decode(FriendlyByteBuf buf) {
        packet.decode(buf);
        return packet;
    }

    public T handle(T self, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var serverPlayer = ctx.get().getSender();
            handler.get().handle(self, serverPlayer);
        });
        ctx.get().setPacketHandled(true);
        return self;
    }
}
