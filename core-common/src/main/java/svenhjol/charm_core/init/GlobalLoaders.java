package svenhjol.charm_core.init;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm_core.iface.ILoader;

import java.util.HashMap;
import java.util.Map;

public class GlobalLoaders {
    public static final Map<String, ILoader> LOADERS = new HashMap<>();

    /**
     * Checks whether a loader contains a given feature and the feature is enabled.
     * @param id ResourceLocation of feature, e.g. {namespace:"charm", path:"SmoothGlowstone"}
     * @return True if feature is present enabled in the specificed loader.
     */
    public static boolean isEnabled(ResourceLocation id) {
        if (LOADERS.containsKey(id.getNamespace())) {
            var loader = LOADERS.get(id.getNamespace());
            return loader.isEnabled(id.toString());
        }

        return false;
    }
}
