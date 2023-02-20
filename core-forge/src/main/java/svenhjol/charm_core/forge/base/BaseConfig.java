package svenhjol.charm_core.forge.base;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import svenhjol.charm_core.annotation.Configurable;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.iface.ILog;

import java.lang.reflect.Field;
import java.util.*;

public abstract class BaseConfig {
    private final ModConfig.Type type;
    private final ILog log;

    public BaseConfig(ILog log, ModConfig.Type type) {
        this.type = type;
        this.log = log;
    }

    private final List<Runnable> refresh = new ArrayList<>();

    public void build(List<CharmFeature> features) {
        if (features.isEmpty()) return;

        var activeContainer = ModLoadingContext.get().getActiveContainer();

        var configSpec = new ForgeConfigSpec.Builder()
            .configure(builder -> build(builder, new LinkedList<>(features))).getRight();

        var modConfig = new ModConfig(type, configSpec, activeContainer);
        activeContainer.addConfig(modConfig);
    }

    public void refresh(ModConfigEvent event) {
        if (event.getConfig().getType().equals(this.type)) {
            refresh.forEach(Runnable::run);
        }
    }

    private Void build(ForgeConfigSpec.Builder builder, List<CharmFeature> features) {
        // Sort alphabetically for configuration.
        features.sort(Comparator.comparing(CharmFeature::getName));

        for (var feature : features) {
            var description = feature.getDescription();
            var switchable = feature.isSwitchable();
            var enabledByDefault = feature.isEnabledByDefault();

            if (!description.isEmpty()) {
                builder.comment(description);
            }

            if (!switchable) {
                feature.setEnabled(true);
                continue;
            }

            var featureEnabledVal = builder.define(
                feature.getName() + " enabled", enabledByDefault
            );

            refresh.add(() -> {
                var enabledInConfig = featureEnabledVal.get();
                feature.setEnabledInConfig(enabledInConfig);
                feature.setEnabled(feature.isEnabled() && enabledInConfig);
            });
        }

        for (var feature : features) {
            builder.push(feature.getName());

            var fields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
            for (Field field : fields) {
                var annotation = field.getDeclaredAnnotation(Configurable.class);
                if (annotation == null) continue;

                field.setAccessible(true);
                var name = annotation.name();
                var description = annotation.description();

                if (name.isEmpty()) {
                    name = field.getName();
                }

                if (!description.isEmpty()) {
                    builder.comment(description);
                }

                try {
                    ForgeConfigSpec.ConfigValue<?> currentVal;
                    Object defaultVal = field.get(null);

                    if (defaultVal instanceof List) {
                        currentVal = builder.defineList(name, (List<?>) defaultVal, o -> true);
                    } else {
                        currentVal = builder.define(name, defaultVal);
                    }

                    var finalName = name;
                    var finalVal = currentVal;

                    refresh.add(() -> {
                        try {
                            log.info(getClass(), "[" + feature.getName() + "] Setting config field " + finalName + " to " + finalVal.get());
                            field.set(null, currentVal.get());
                        } catch (IllegalAccessException e) {
                            log.error(getClass(), "Could not set config value for " + feature.getName());
                            throw new RuntimeException(e);
                        }
                    });

                } catch (ReflectiveOperationException e) {
                    log.error(getClass(), "Failed to get config for " + feature.getName());
                }
            }

            builder.pop();
        }

        return null;
    }
}
