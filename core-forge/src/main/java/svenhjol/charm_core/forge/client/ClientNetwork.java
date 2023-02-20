package svenhjol.charm_core.forge.client;

import svenhjol.charm_core.forge.server.ServerNetwork;
import svenhjol.charm_core.iface.IClientNetwork;
import svenhjol.charm_core.iface.IPacketRequest;

public class ClientNetwork implements IClientNetwork {
    @Override
    public void send(IPacketRequest packet) {
        ServerNetwork.INSTANCE.sendToServer(packet);
    }
}
