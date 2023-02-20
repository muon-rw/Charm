package svenhjol.charm_core.fabric.server;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm_core.iface.INetwork;
import svenhjol.charm_core.iface.IPacketRequest;

import javax.annotation.Nullable;

public class ServerNetwork implements INetwork {
    @Override
    public void send(IPacketRequest packet, @Nullable Player player) {
        if (player != null) {
            var buffer = new FriendlyByteBuf(Unpooled.buffer());
            packet.encode(buffer);
            ServerPlayNetworking.send((ServerPlayer) player, packet.id(), buffer);
        }
    }
}
