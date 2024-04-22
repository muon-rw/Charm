package svenhjol.charm.foundation;

public abstract class Network<T extends Feature> {
    protected T feature;

    public Network(T feature) {
        this.feature = feature;
    }

    public int priority() {
        return 0;
    }

    public abstract void onRegister();
}
