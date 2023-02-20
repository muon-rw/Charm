package svenhjol.charm_core.iface;

public interface IClientInitializer {
    String getNamespace();

    String getRegistryNamespace();

    default ILog getLog() {
        throw new AssertionError();
    }

    default IEvents getEvents() {
        throw new AssertionError();
    }

    default ILoader getLoader() {
        throw new AssertionError();
    }

    default IClientRegistry getRegistry() {
        throw new AssertionError();
    }

    default IClientNetwork getNetwork() {
        throw new AssertionError();
    }
}
