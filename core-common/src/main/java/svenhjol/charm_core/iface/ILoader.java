package svenhjol.charm_core.iface;

import svenhjol.charm_core.base.CharmFeature;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public interface ILoader {
    void init(String packageName, Class<? extends Annotation> annotation);

    void run();

    boolean isEnabled(Class<? extends CharmFeature> feature);

    boolean isEnabled(String name);

    List<CharmFeature> getFeatures();

    List<CharmFeature> getEnabledFeatures();

    List<CharmFeature> getDisabledFeatures();

    <T extends CharmFeature> Optional<T> get(Class<T> clazz);
}
