package com.andcreations.ae.studio.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A finalizer plugin is started at the end.
 * 
 * @author Mikolaj Gucki
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Finalizer {
}