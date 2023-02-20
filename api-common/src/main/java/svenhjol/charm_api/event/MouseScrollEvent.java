package svenhjol.charm_api.event;

public class MouseScrollEvent {
    public static final OnScreenEvent ON_SCREEN = new OnScreenEvent();
    public static final OffScreenEvent OFF_SCREEN = new OffScreenEvent();

    public static class OnScreenEvent extends CharmEvent<Handler> {
        private OnScreenEvent() {}

        public void invoke(double direction) {
            for (Handler handler : getHandlers()) {
                handler.run(direction);
            }
        }
    }

    public static class OffScreenEvent extends CharmEvent<Handler> {
        private OffScreenEvent() {}

        public void invoke(double direction) {
            for (Handler handler : getHandlers()) {
                handler.run(direction);
            }
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(double direction);
    }
}
