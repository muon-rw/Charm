package svenhjol.charm_api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue", "unchecked"})
public class CharmApi {
    private static final Logger LOGGER = LogManager.getLogger("CharmApi");
    private static final String API_HANDLER = "svenhjol.charm_core.init.CharmApi";

    public static void registerProvider(Object provider) {
        call(API_HANDLER, "registerProvider", List.of(provider), List.of(Object.class));
    }

    public static <T> T call(String className, String targetMethod) {
        return call(className, targetMethod, List.of(), List.of());
    }

    public static <T> T call(String targetClass, String targetMethod, List<Object> args, List<Class<?>> types) {
        try {
            var clazz = Class.forName(targetClass);
            var method = clazz.getMethod(targetMethod, types.toArray(new Class<?>[0]));
            return (T)method.invoke(null, args.toArray(new Object[0]));
        } catch (Exception e) {
            // Package probably not installed, just skip.
            LOGGER.debug("Call failed. targetClass: " + targetClass + ", targetMethod: " + targetMethod);
        }
        return null;
    }
}
