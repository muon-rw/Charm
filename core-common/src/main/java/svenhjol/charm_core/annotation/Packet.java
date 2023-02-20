package svenhjol.charm_core.annotation;

import svenhjol.charm_core.enums.PacketDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Packet {
    String id() default "";
    String description() default "";
    PacketDirection direction();
}
