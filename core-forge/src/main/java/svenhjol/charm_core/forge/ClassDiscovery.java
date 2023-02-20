package svenhjol.charm_core.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class ClassDiscovery {
    protected static final Logger LOGGER = LogManager.getLogger("ClassDiscovery");

    public static <T> List<Class<T>> getClassesInMod(String modId, String modPackageName, Class<? extends Annotation> matchAnnotation) {
        var scanData = ModList.get().getAllScanData();
        var opt = scanData.stream()
            .filter(s -> s.getTargets().containsKey(modId))
            .findFirst();

        if (opt.isPresent()) {
            return getClassesWithAnnotation(opt.get(), matchAnnotation);
        }

        return List.of();
    }

    public static <T> List<Class<T>> getClassesInPackage(String packageName, Class<? extends Annotation> matchAnnotation) {
        return getClassesInPackage(Thread.currentThread().getContextClassLoader(), packageName, matchAnnotation);
    }

    public static <T> List<Class<T>> getClassesInPackage(ClassLoader classLoader, String packageName, Class<? extends Annotation> matchAnnotation) {
        var scanData = ModList.get().getAllScanData();
        var split = packageName.split("\\.");
        String mod;

        if (split.length > 3) {
            // e.g.:
            // - svenhjol.charm.tweaks.feature => charm_tweaks,
            // - svenhjol.charm.blah.something.feature => charm_blah_something
            var builder = new StringBuilder(split[1]);
            for (var i = 2; i < split.length - 1; i++) {
                builder.append("_").append(split[i]);
            }
            mod = builder.toString();
        } else if (split.length == 3) {
            // e.g. svenhjol.charm.feature => charm
            mod = split[1];
        } else {
            throw new RuntimeException("I can't handle package names with depth less than 3.");
        }

        var opt = scanData.stream()
            .filter(s -> s.getTargets().containsKey(mod))
            .findFirst();

        if (opt.isPresent()) {
            return getClassesWithAnnotation(opt.get(), matchAnnotation);
        }

        return List.of();
    }

    private static <T> List<Class<T>> getClassesWithAnnotation(ModFileScanData scan, Class<? extends Annotation> matchAnnotation) {
        var annotations = scan.getAnnotations();
        return annotations.stream()
            .filter(a -> a.annotationType().getClassName().equals(matchAnnotation.getName()))
            .map(a -> {
                try {
                    return (Class<T>)Class.forName(a.clazz().getClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();
    }
}
