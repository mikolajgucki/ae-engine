package com.andcreations.ae.studio.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An initializer plugin is started at the beginning.
 * 
 * @author Mikolaj Gucki
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Initializer {
}