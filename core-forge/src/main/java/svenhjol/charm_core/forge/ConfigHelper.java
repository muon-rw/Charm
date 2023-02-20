package svenhjol.charm_core.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ConfigHelper {
    private static final List<String> LINES = new ArrayList<>();

    public static boolean isModLoaded(String modName) {
        var modList = ModList.get();

        if (modList != null) {
            return modList.isLoaded(modName);
        }

        return false;
    }

    public static boolean isFeatureEnabled(String modId, String featureName) {
        if (LINES.isEmpty()) {
            readConfigFile(modId);
        }

        for (var line : LINES) {
            if (!line.contains("enabled")) continue;
            if (line.contains(featureName)) {
                if (line.contains("false")) {
                    return false;
                } else if (line.contains("true")) {
                    return true;
                }
            }
        }

        return true;
    }
    
    public static boolean configExists(String modId) {
        return getConfigPath(modId).toFile().exists();
    }

    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    static Path getConfigPath(String modId) {
        return Paths.get(getConfigDir() + "/" + modId + "-common.toml");
    }

    static List<String> getConfigLines() {
        return LINES;
    }

    static void readConfigFile(String modId) {
        var path = getConfigPath(modId);
        var file = path.toFile();

        if (file.exists()) {
            try {
                LINES.clear();
                LINES.addAll(Files.readAllLines(path));
            } catch (Exception e) {
                // no
            }
        }
    }
}