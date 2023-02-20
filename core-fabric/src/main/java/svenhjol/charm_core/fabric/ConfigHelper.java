package svenhjol.charm_core.fabric;

import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigHelper {
    public static boolean isModLoaded(String modName) {
        FabricLoader instance = FabricLoader.getInstance();
        return instance.isModLoaded(modName);
    }

    public static boolean isFeatureEnabled(String modId, String featureName) {
        var toml = read(modId);
        var quoted = getQuotedFeatureEnabledName(featureName);
        
        if (toml.contains(quoted)) {
            return toml.getBoolean(quoted);
        }

        return true;
    }
    
    public static boolean configExists(String modId) {
        return getConfigPath(modId).toFile().exists();
    }

    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    static Toml read(String modId) {
        var toml = new Toml();
        var path = getConfigPath(modId);
        var file = path.toFile();

        if (!file.exists()) {
            return toml;
        }

        return toml.read(file);
    }

    static Path getConfigPath(String modId) {
        return Paths.get(getConfigDir() + "/" + modId + ".toml");
    }

    static String getQuotedFeatureEnabledName(String featureName) {
        var featureEnabled = featureName + " enabled";
        return "\"" + featureEnabled + "\"";
    }
}