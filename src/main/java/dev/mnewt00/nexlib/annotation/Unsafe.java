package dev.mnewt00.nexlib.annotation;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

/**
 * A program element annotated &#64;Unsafe is meant for
 * experimental purposes as it is unstable or hence the name,
 * unsafe.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
public @interface Unsafe {
}
