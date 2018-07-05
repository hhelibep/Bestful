package pers.hhelibep.Bestful.core.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { PARAMETER, FIELD })
public @interface StringAttributeLimitation {
    
    boolean required() default (true);
}
