package svenhjol.charm_core.iface;

import net.minecraft.world.entity.player.Player;

public interface INetwork {
    void send(IPacketRequest packet, Player player);
}
