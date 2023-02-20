package svenhjol.charm_core.forge.common;

import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.base.CharmLoader;
import svenhjol.charm_core.forge.DebugHelper;
import svenhjol.charm_core.forge.base.BaseConfig;
import svenhjol.charm_core.iface.IInitializer;
import svenhjol.charm_core.iface.ILog;
import svenhjol.charm_core.iface.IRegistry;
import svenhjol.charm_core.init.GlobalLoaders;

import java.util.Comparator;
import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class CommonLoader extends CharmLoader {
    private final ILog log;
    private final IRegistry registry;
    private final BaseConfig config;

    public CommonLoader(IInitializer init, BaseConfig config) {
        this.log = init.getLog();
        this.registry = init.getRegistry();
        this.config = config;
    }

    @Override
    protected boolean featureSetup(CharmFeature feature) {
        var clazz = feature.getClass();

        if (clazz.isAnnotationPresent(Feature.class)) {
            var annotation = clazz.getAnnotation(Feature.class);

            // Set up a common feature.
            feature.setModId(annotation.mod());
            feature.setDescription(annotation.description());
            feature.setPriority(annotation.priority());
            feature.setSwitchable(annotation.switchable());
            feature.setEnabledByDefault(annotation.enabledByDefault());
            feature.setEnabled(annotation.enabledByDefault());

            return true;
        }

        return false;
    }

    @Override
    protected void preRegister() {
        // Sort into priority order for register.
        features.sort(Comparator.comparing(CharmFeature::getPriority).reversed());

        // Run pre-registration.
        features.forEach(CharmFeature::preRegister);
    }

    @Override
    protected void register() {
        features.forEach(CharmFeature::register);

        // Add this loader to global loaders.
        if (!features.isEmpty()) {
            GlobalLoaders.LOADERS.put(features.getFirst().getModId(), this);
        }
    }

    @Override
    protected void configure() {
        // Sort alphabetically for configuration.
        features.sort(Comparator.comparing(CharmFeature::getName));
        config.build(getFeatures());
    }

    @Override
    protected void check() {
        var debug = DebugHelper.isEnabled();

        for (var feature : features) {
            var enabledInConfig = feature.isEnabledInConfig();
            var passedCheck = feature.checks().isEmpty() || feature.checks().stream().allMatch(BooleanSupplier::getAsBoolean);

            feature.setEnabled(enabledInConfig && passedCheck);

            if (!enabledInConfig) {
                if (debug) getLog().warn(getClass(), "Disabled in configuration: " + feature.getName());
            } else if (!passedCheck) {
                if (debug) getLog().warn(getClass(), "Failed check: " + feature.getName());
            } else if (!feature.isEnabled()) {
                if (debug) getLog().warn(getClass(), "Disabled automatically: " + feature.getName());
            } else {
                getLog().info(getClass(), "Enabled " + feature.getName());
            }
        }
    }

    @Override
    protected ILog getLog() {
        return log;
    }
}
