package svenhjol.charm_core.iface;

import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface IPacketHandler<T extends IPacketRequest> {
    /**
     * Handle network packet.
     * @param message Original network packet.
     * @param player ServerPlayer on the server side, LocalPlayer on the client side.
     */
    void handle(T message, Player player);
}