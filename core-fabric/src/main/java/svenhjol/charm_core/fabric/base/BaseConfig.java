package svenhjol.charm_core.fabric.base;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlWriter;
import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charm_core.annotation.Configurable;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.fabric.DebugHelper;
import svenhjol.charm_core.iface.ILog;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class BaseConfig {
    private final String filename;
    private final ILog log;
    private static final Map<Field, Object> DEFAULT_FIELD_VALUES = new HashMap<>();
    private boolean hasAppliedConfig = false;

    public BaseConfig(ILog log, String filename) {
        this.filename = filename;
        this.log = log;
    }

    public void readConfig(List<CharmFeature> features) {
        var toml = read();

        // Apply values from the config file to the features.
        for (var feature : features) {
            var featureName = feature.getName();

            // Update the enabledInConfig property of each feature.
            if ((toml.isEmpty() || !featureExistsInConfig(toml, featureName)) && !feature.isEnabledByDefault()) {
                feature.setEnabledInConfig(false);
            } else {
                feature.setEnabledInConfig(!isFeatureDisabled(toml, featureName));
            }

            // Process fields in the feature that have the @Config annotation.
            var fields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
            for (var field : fields) {
                try {
                    // Ignore fields that don't have the Config annotation.
                    var annotation = field.getDeclaredAnnotation(Configurable.class);
                    if (annotation == null) continue;

                    // set the static property as writable so that the config can modify it
                    field.setAccessible(true);
                    var fieldName = annotation.name();

                    if (fieldName.isEmpty()) {
                        fieldName = field.getName();
                    }

                    Object fieldValue = field.get(null);
                    Object configValue;

                    if (!hasAppliedConfig) {
                        DEFAULT_FIELD_VALUES.put(field, fieldValue);
                    }

                    // Get the config values that were set in the toml file and apply them to the feature objects.
                    if (toml.contains(featureName)) {

                        // Get the set of key/value pairs for this feature.
                        var featureKeys = toml.getTable(featureName);
                        Map<String, Object> mappedKeys = new HashMap<>();

                        // Key names sometimes have quotes, map to remove them.
                        featureKeys.toMap().forEach((k, v) -> mappedKeys.put(k.replace("\"", ""), v));
                        configValue = mappedKeys.get(fieldName);

                        if (configValue != null) {
                            // There's some weirdness with casting, deal with that here.
                            if (fieldValue instanceof Integer && configValue instanceof Double) {
                                configValue = (int) (double) configValue;
                            }

                            if (fieldValue instanceof Integer && configValue instanceof Long) {
                                configValue = (int) (long) configValue;
                            }

                            if (fieldValue instanceof Float && configValue instanceof Double) {
                                configValue = (float) (double) configValue;
                            }

                            // Set the class property.
                            if (DebugHelper.isEnabled()) {
                                log.info(getClass(), "In feature " + featureName + ": setting `" + fieldName + "` to `" + configValue + "`");
                            }
                            field.set(null, configValue);
                        }
                    }

                } catch (Exception e) {
                    log.warn(getClass(), "Failed to read config field in feature " + featureName + ": " + e.getMessage());
                }
            }
        }

        hasAppliedConfig = true;
    }

    public void writeConfig(List<CharmFeature> features) {
        // This blank config is appended and then written out. LinkedHashMap supplier sorts the contents alphabetically.
        var config = TomlFormat.newConfig(LinkedHashMap::new);

        for (var feature : features) {
            var featureName = feature.getName();

            if (feature.isSwitchable()) {
                String field = featureName + " enabled";
                config.setComment(field, feature.getDescription());
                config.add(field, feature.isEnabledInConfig());
            }

            // Read feature field values into the blank config.
            var fields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
            for (var field : fields) {
                try {
                    var annotation = field.getDeclaredAnnotation(Configurable.class);
                    if (annotation == null) continue;

                    var fieldName = annotation.name();
                    var fieldDescription = annotation.description();
                    Object fieldValue = field.get(null);

                    // set the key/value pair. The "." specifies that it is nested
                    var featureConfigName = featureName + "." + fieldName;
                    config.setComment(featureConfigName, fieldDescription);
                    config.add(featureConfigName, fieldValue);

                } catch (Exception e) {
                    log.error(getClass(), "Failed to write config property `" + field.getName() + "` in `" + feature.getName() + "`");
                }
            }
        }

        if (!config.isEmpty()) {
            write(config);
        }
    }

    private Toml read() {
        var toml = new Toml();
        var path = getConfigPath();
        var file = path.toFile();

        if (!file.exists()) {
           return toml;
        }

        return toml.read(file);
    }

    private void write(CommentedConfig toml) {
        var path = getConfigPath();

        try {
            // Write out and close the file.
            var tomlWriter = new TomlWriter();
            var buffer = Files.newBufferedWriter(path);

            tomlWriter.write(toml, buffer);
            buffer.close();
            log.debug(getClass(), "Written config to disk");
        } catch (Exception e) {
            log.error(getClass(), "Failed to write config: " + e.getMessage());
        }
    }

    private Path getConfigPath() {
        return Paths.get(FabricLoader.getInstance().getConfigDir() + "/" + filename + ".toml");
    }

    /**
     * Even though it seems that it should be isFeatureEnabled and the NOT check removed,
     * this needs to be an explicit disabled check so that early mixin loading works.
     */
    public static boolean isFeatureDisabled(Toml toml, String featureName) {
        var quoted = getQuotedFeatureEnabledName(featureName);
        return toml.contains(quoted) && !toml.getBoolean(quoted);
    }

    private static boolean featureExistsInConfig(Toml toml, String featureName) {
        var quoted = getQuotedFeatureEnabledName(featureName);
        return toml.contains(quoted);
    }

    private static String getQuotedFeatureEnabledName(String featureName) {
        var featureEnabled = featureName + " enabled";
        return "\"" + featureEnabled + "\"";
    }
}
