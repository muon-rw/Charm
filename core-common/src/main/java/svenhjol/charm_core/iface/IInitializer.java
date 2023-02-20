package svenhjol.charm_core.iface;

public interface IInitializer {
    String getNamespace();

    String getRegistryNamespace();

    default ILog getLog() {
        throw new AssertionError();
    }

    default IRegistry getRegistry() {
        throw new AssertionError();
    }

    default IEvents getEvents() {
        throw new AssertionError();
    }

    default ILoader getLoader() {
        throw new AssertionError();
    }

    default INetwork getNetwork() {
        throw new AssertionError();
    }
}
