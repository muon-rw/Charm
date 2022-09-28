package svenhjol.charm.module.ender_bundles.network;

import svenhjol.charm.network.ClientSender;
import svenhjol.charm.network.Id;

@Id("charm:request_ender_inventory")
public class ClientSendRequestEnderInventory extends ClientSender {
    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}