package svenhjol.charm_core.iface;

import svenhjol.charm_core.proxy.DebugHelperProxy;

public interface ILog {
    void info(String message, Object... args);

    void info(Class<?> source, String message, Object... args);

    void warn(String message, Object... args);

    void warn(Class<?> source, String message, Object... args);

    void error(String message, Object... args);

    void error(Class<?> source, String message, Object... args);

    default void debug(Class<?> source, String message, Object... args) {
        if (DebugHelperProxy.isEnabled()) {
            info(source, message, args);
        }
    }

    default String makeMessage(Class<?> source, String message) {
        return "[" + source.getSimpleName() + "] " + message;
    }
}
