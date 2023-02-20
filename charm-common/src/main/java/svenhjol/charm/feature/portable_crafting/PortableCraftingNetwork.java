package svenhjol.charm.feature.portable_crafting;

import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.Packet;
import svenhjol.charm_core.enums.PacketDirection;
import svenhjol.charm_core.iface.IPacketRequest;

public class PortableCraftingNetwork {
    public static void register() {
        Charm.REGISTRY.packet(new OpenCrafting(), () -> PortableCrafting::handleOpenedCraftingTable);
    }

    @Packet(
        id = "charm:open_portable_crafting",
        direction = PacketDirection.CLIENT_TO_SERVER,
        description = "An empty packet sent from the client to instruct the server to open the portable crafting menu."
    )
    public static class OpenCrafting implements IPacketRequest {
        private OpenCrafting() {}

        public static void send() {
            CharmClient.NETWORK.send(new OpenCrafting());
        }
    }
}
