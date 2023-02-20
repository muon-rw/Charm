package svenhjol.charm_core.fabric;

import com.google.common.io.ByteSource;
import com.google.common.reflect.ClassPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"UnstableApiUsage", "unchecked", "unused"})
public class ClassDiscovery {
    protected static final Logger LOGGER = LogManager.getLogger("ClassDiscovery");

    public static <T> List<Class<T>> getClassesInMod(String modId, String modPackageName, Class<? extends Annotation> matchAnnotation) {
        return getClassesInPackage(modPackageName, matchAnnotation);
    }

    public static <T> List<Class<T>> getClassesInPackage(String packageName, Class<? extends Annotation> matchAnnotation) {
        return getClassesInPackage(Thread.currentThread().getContextClassLoader(), packageName, matchAnnotation);
    }

    public static <T> List<Class<T>> getClassesInPackage(ClassLoader classLoader, String packageName, Class<? extends Annotation> matchAnnotation) {
        List<? extends ClassInfo> set;

        try {
            var from = ClassPath.from(classLoader);
            set = from.getTopLevelClassesRecursive(packageName)
                .stream().map(DelegatedClassInfo::new).toList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        var annotationName = "L" + matchAnnotation.getName().replace(".", "/") + ";";
        List<Class<T>> discoveredClasses = new ArrayList<>();

        for (var c : set) {
            var className = c.getName();
            var truncatedName = className.substring(packageName.length() + 1);

            try {
                var classReader = new ClassReader(c.asByteSource().read());
                var node = new ClassNode();
                classReader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                if (node.visibleAnnotations != null && !node.visibleAnnotations.isEmpty()) {
                    for (var annotation : node.visibleAnnotations) {
                        if (annotation.desc.equals(annotationName)) {
                            discoveredClasses.add((Class<T>) Class.forName(c.getName()));
                        }
                    }
                }

            } catch (Exception e) {
                LOGGER.error("Error occurred while processing class " + truncatedName + ": " + e.getMessage());
            }
        }

        return discoveredClasses;
    }

    public interface ClassInfo {
        String getName();
        ByteSource asByteSource();
    }

    public record DelegatedClassInfo(ClassPath.ClassInfo delegate) implements ClassInfo {
        @Override
        public String getName() {
            return delegate.getName();
        }

        @Override
        public ByteSource asByteSource() {
            return delegate.asByteSource();
        }
    }
}
