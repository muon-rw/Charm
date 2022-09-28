package svenhjol.charm.module.waypoints.network;

import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerSender;

@Id("strange:flush_waypoint")
public class ServerSendFlushWaypoint extends ServerSender {
    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}
