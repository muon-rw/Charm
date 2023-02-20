package svenhjol.charm_core.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ClientFeature {
    String mod() default "";

    int priority() default 0;

    String description() default "";

    boolean switchable() default false;
}
