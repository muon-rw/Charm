package svenhjol.charm_api.iface;

import java.util.List;

public interface IProvidesWandererTrades {
    List<IWandererTrade> getWandererTrades();

    List<IWandererTrade> getRareWandererTrades();
}
