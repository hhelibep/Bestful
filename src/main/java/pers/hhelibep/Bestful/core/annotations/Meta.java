package pers.hhelibep.Bestful.core.annotations;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pers.hhelibep.Bestful.core.AuthType;
import pers.hhelibep.Bestful.core.HttpMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD })
public @interface Meta {
    HttpMethod method();

    String path() default "";

    AuthType authType() default AuthType.Base64;

}
