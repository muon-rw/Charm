package svenhjol.charm_core.forge.server;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import svenhjol.charm_core.CharmCore;
import svenhjol.charm_core.iface.INetwork;
import svenhjol.charm_core.iface.IPacketRequest;

public class ServerNetwork implements INetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(CharmCore.MOD_ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    @Override
    public void send(IPacketRequest packet, Player player) {
        if (!player.level.isClientSide()) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), packet);
        }
    }
}
