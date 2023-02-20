package svenhjol.charm_core.fabric.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm_core.iface.IClientNetwork;
import svenhjol.charm_core.iface.IPacketRequest;

public class ClientNetwork implements IClientNetwork {
    @Override
    public void send(IPacketRequest packet) {
        var buffer = new FriendlyByteBuf(Unpooled.buffer());
        packet.encode(buffer);
        ClientPlayNetworking.send(packet.id(), buffer);
    }
}
