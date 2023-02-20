package svenhjol.charm_core.proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Locale;

@SuppressWarnings({"unchecked", "unused"})
public class Proxy {
    Logger LOGGER = LogManager.getLogger("Proxy");
    Environment environment;

    public static Proxy INSTANCE = new Proxy();

    private Proxy() {
        try {
            Class.forName("cpw.mods.modlauncher.serviceapi.ILaunchPluginService");
            environment = Environment.FORGE;
        } catch (ClassNotFoundException e) {
            // Try next.
        }

        try {
            Class.forName("net.fabricmc.loader.api.FabricLoader");
            environment = Environment.FABRIC;
        } catch (ClassNotFoundException e) {
            // Try next.
        }

        if (environment == null) {
            var message = "Could not determine the environment Charm is running on.";
            LOGGER.error(message);
            throw new RuntimeException(message);
        }
    }

    public <T> T constructor(String prefix, String className) {
        return constructor(prefix, className, List.of(), List.of());
    }

    public <T> T constructor(String prefix, String className, List<Object> args, List<Class<?>> types) {
        var environmentName = environment.getName();
        var targetClass = prefix + "." + environmentName + "." + className;

        try {
            var clazz = Class.forName(targetClass);
            Object out;

            if (args.isEmpty()) {
                out = clazz.getDeclaredConstructor().newInstance();
            } else {
                out = clazz.getDeclaredConstructor(types.toArray(new Class<?>[0]))
                    .newInstance(args.toArray(new Object[0]));
            }
            return (T)out;
        } catch (Exception e) {
            var message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            LOGGER.error("Failed to create instance via " + environmentName + ": " + message + ", targetClass: " + targetClass);
            throw new RuntimeException(message);
        }
    }

    public <T> T method(String prefix, String className, String targetMethod) {
        return method(prefix, className, targetMethod, List.of(), List.of());
    }

    public <T> T method(String prefix, String className, String targetMethod, List<Object> args, List<Class<?>> types) {
        // Add prefix according to environment.
        var environmentName = environment.getName();
        var targetClass = prefix + "." + environmentName + "." + className;

        try {
            var clazz = Class.forName(targetClass);
            var method = clazz.getMethod(targetMethod, types.toArray(new Class<?>[0]));
            var out = method.invoke(null, args.toArray(new Object[0]));
            return (T)out;
        } catch (Exception e) {
            var message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            LOGGER.error("Failed to static call via " + environmentName + ": " + message + ", targetClass: " + targetClass + ", targetMethod: " + targetMethod);
            throw new RuntimeException(message);
        }
    }

    enum Environment {
        FORGE("forge"),
        FABRIC("fabric");

        final String name;

        Environment(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name.toLowerCase(Locale.ROOT);
        }
    }
}
