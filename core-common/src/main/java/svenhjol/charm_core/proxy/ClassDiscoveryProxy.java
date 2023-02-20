package svenhjol.charm_core.proxy;

import svenhjol.charm_core.CharmCore;

import java.lang.annotation.Annotation;
import java.util.List;

public class ClassDiscoveryProxy {
    public static <T> List<Class<T>> getClassesInMod(String modId, String modPackageName, Class<? extends Annotation> matchAnnotation) {
        return Proxy.INSTANCE.method(CharmCore.PREFIX, "ClassDiscovery", "getClassesInMod",
            List.of(modId, modPackageName, matchAnnotation), List.of(String.class, String.class, Class.class));
    }

    public static <T> List<Class<T>> getClassesInPackage(String packageName, Class<? extends Annotation> matchAnnotation) {
        return Proxy.INSTANCE.method(CharmCore.PREFIX, "ClassDiscovery", "getClassesInPackage",
            List.of(packageName, matchAnnotation), List.of(String.class, Class.class));
    }

    public static <T> List<Class<T>> getClassesInPackage(ClassLoader classLoader, String packageName, Class<? extends Annotation> matchAnnotation) {
        return Proxy.INSTANCE.method(CharmCore.PREFIX, "ClassDiscovery", "getClassesInPackage",
            List.of(classLoader, packageName, matchAnnotation), List.of(ClassLoader.class, String.class, Class.class));
    }
}
