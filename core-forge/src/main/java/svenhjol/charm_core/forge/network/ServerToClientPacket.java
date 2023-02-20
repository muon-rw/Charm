package svenhjol.charm_core.forge.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import svenhjol.charm_core.CharmCore;
import svenhjol.charm_core.iface.IPacketHandler;
import svenhjol.charm_core.iface.IPacketRequest;

import java.util.function.Supplier;

public class ServerToClientPacket<T extends IPacketRequest, U extends IPacketHandler<T>> {
    private final T packet;
    private final Supplier<U> handler;

    public ServerToClientPacket(T packet, Supplier<U> handler) {
        this.packet = packet;
        this.handler = handler;
    }

    public void encode(T self, FriendlyByteBuf buf) {
        self.encode(buf);
    }

    public T decode(FriendlyByteBuf buf) {
        try {
            packet.decode(buf);
        } catch (Exception e) {
            CharmCore.LOG.debug(getClass(), "Something went wrong when decoding packet. Did it encode properly?");
        }
        return packet;
    }

    public T handle(T self, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(
            () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> WrappedClientCall.handle(handler, self)));
        
        ctx.get().setPacketHandled(true);
        return self;
    }
}