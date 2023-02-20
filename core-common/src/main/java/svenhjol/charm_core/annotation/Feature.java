package svenhjol.charm_core.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Feature {
    String mod() default "";

    String description() default "";

    int priority() default 0;

    boolean switchable() default true;

    boolean enabledByDefault() default true;
}
