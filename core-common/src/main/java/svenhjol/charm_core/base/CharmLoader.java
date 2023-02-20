package svenhjol.charm_core.base;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm_core.proxy.ClassDiscoveryProxy;
import svenhjol.charm_core.iface.ILoader;
import svenhjol.charm_core.iface.ILog;
import svenhjol.charm_core.init.AdvancementHandler;
import svenhjol.charm_core.init.GlobalLoaders;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CharmLoader implements ILoader {
    protected final LinkedList<CharmFeature> features = new LinkedList<>();

    @Override
    public void init(String packageName, Class<? extends Annotation> annotation) {
        // Autoload each feature from the jar file.
        classLoader(packageName, annotation);

        // Read/write all the loader's features to a config file.
        configure();

        // Run each feature's dependency checks.
        check();

        // Allow features to add things such as enums that are required before registration.
        preRegister();

        // Register items, blocks, structures etc.
        register();
    }

    @Override
    public void run() {
        var enabled = getEnabledFeatures();
        for (var feature : enabled) {
            getLog().info(getClass(), "Running " + feature.getName());
            feature.runWhenEnabled();
        }

        var disabled = getDisabledFeatures();
        for (var feature : disabled) {
            getLog().debug(getClass(), "Running disabled tasks for " + feature.getName());
            feature.runWhenDisabled();

            // Remove advancements for disabled features.
            AdvancementHandler.remove(feature);
        }

        for (CharmFeature feature : getFeatures()) {
            feature.runAlways();
        }
    }

    /**
     * Checks if a feature is enabled in this loader by its class name.
     * @param feature Name of feature in pascal case.
     * @return True if feature is enabled in this loader.
     */
    @Override
    public boolean isEnabled(Class<? extends CharmFeature> feature) {
        return getFeatures().stream().anyMatch(
            m -> m.getClass().equals(feature) && m.isEnabled());
    }

    /**
     * Checks if a feature is enabled in this loader by its name.
     * @param name Name of feature in pascal case.
     * @return True if feature is enabled in this loader.
     */
    @Override
    public boolean isEnabled(String name) {
        return getFeatures().stream().anyMatch(
            m -> m.getName().equals(name) && m.isEnabled());
    }

    /**
     * Pass-through to GlobalLoaders.isEnabled().
     * Checks whether a loader contains a given feature and the feature is enabled.
     * @param id ResourceLocation of feature, e.g. {namespace:"charm", path:"SmoothGlowstone"}
     * @return True if feature is present enabled in the specificed loader.
     */
    public boolean isEnabled(ResourceLocation id) {
        return GlobalLoaders.isEnabled(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CharmFeature> Optional<T> get(Class<T> clazz) {
        var first = features.stream()
            .filter(m -> m.getClass().equals(clazz))
            .findFirst();

        return (Optional<T>)first;
    }

    @Override
    public List<CharmFeature> getFeatures() {
        return features;
    }

    @Override
    public List<CharmFeature> getEnabledFeatures() {
        return features.stream().filter(CharmFeature::isEnabled).collect(Collectors.toList());
    }

    @Override
    public List<CharmFeature> getDisabledFeatures() {
        return features.stream().filter(m -> !m.isEnabled()).collect(Collectors.toList());
    }

    protected void classLoader(String packageName, Class<? extends Annotation> annotation) {
        List<Class<CharmFeature>> classes = ClassDiscoveryProxy.getClassesInPackage(packageName, annotation);

        if (classes.isEmpty()) {
            getLog().warn(getClass(), "No classes to load for " + packageName);
            return;
        } else {
            var size = classes.size();
            getLog().info(getClass(), size + " class" + (size > 1 ? "es" : "") + " to load for " + packageName);
        }

        for (Class<? extends CharmFeature> clazz : classes) {
            try {
                var feature = clazz.getDeclaredConstructor().newInstance();
                var result = featureSetup(feature);

                if (!result) {
                    var message = "Failed to initialize feature: " + feature.getClass();
                    getLog().error(getClass(), message);
                    throw new RuntimeException(message);
                }

                // Add the instantiated feature to the set of loaded features.
                getFeatures().add(feature);

            } catch (Exception e) {
                var cause = e.getCause();
                var message = cause != null ? cause.getMessage() : e.getMessage();
                getLog().error(getClass(), "Failed to initialize feature " + clazz + ": " + message);
                throw new RuntimeException(message);
            }
        }
    }

    protected boolean featureSetup(CharmFeature feature) {
        return true;
    }

    protected void configure() {
        // no op
    }

    protected void preRegister() {
        // no op
    }

    protected void register() {
        // no op
    }

    protected void check() {
        // no op
    }

    protected abstract ILog getLog();
}
