package svenhjol.charm_core.forge.client;

import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.base.CharmLoader;
import svenhjol.charm_core.forge.base.BaseConfig;
import svenhjol.charm_core.iface.IClientInitializer;
import svenhjol.charm_core.iface.IClientRegistry;
import svenhjol.charm_core.iface.ILog;

import java.util.Comparator;

public class ClientLoader extends CharmLoader {
    private final ILog log;
    private final IClientRegistry registry;
    private final BaseConfig config;

    public ClientLoader(IClientInitializer init, BaseConfig config) {
        this.log = init.getLog();
        this.registry = init.getRegistry();
        this.config = config;
    }

    @Override
    protected boolean featureSetup(CharmFeature feature) {
        var clazz = feature.getClass();

        if (clazz.isAnnotationPresent(ClientFeature.class)) {
            var annotation = clazz.getAnnotation(ClientFeature.class);

            // Set up a client feature.
            feature.setModId(annotation.mod());
            feature.setPriority(annotation.priority());
            feature.setDescription(annotation.description());
            feature.setSwitchable(annotation.switchable());

            return true;
        }

        return false;
    }

    @Override
    protected void register() {
        features.forEach(CharmFeature::register);
    }

    @Override
    protected void preRegister() {
        // Sort into priority order for register.
        features.sort(Comparator.comparing(CharmFeature::getPriority).reversed());

        // Run pre-registration.
        features.forEach(CharmFeature::preRegister);
    }

    @Override
    protected void configure() {
        // Sort alphabetically for configuration.
        features.sort(Comparator.comparing(CharmFeature::getName));
        config.build(getFeatures());
    }

    @Override
    protected ILog getLog() {
        return log;
    }
}
