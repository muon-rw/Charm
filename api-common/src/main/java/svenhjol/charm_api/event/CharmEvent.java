package svenhjol.charm_api.event;

import java.util.ArrayList;
import java.util.List;

public abstract class CharmEvent<T> {
    private final List<T> handlers = new ArrayList<>();

    public List<T> getHandlers() {
        return handlers;
    }

    public void handle(T handler) {
        handlers.add(handler);
    }
}
