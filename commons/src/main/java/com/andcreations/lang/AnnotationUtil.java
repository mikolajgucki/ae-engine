package com.andcreations.lang;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mikolaj Gucki
 */ 
public class AnnotationUtil {
    /**
     * Checks if an annotation is of type.
     * 
     * @param annotation The annotation.
     * @param required The required annotation type.
     * @return <code>true</code> or <code>false</code> respectively.
     */
    public static boolean is(Annotation annotation,
        Class<? extends Annotation> required) {
    //
        return required.getName().equals(annotation.annotationType().getName());
    }
    
    public static Object getValue(Annotation annotation,String name) {
        Class<? extends Annotation> type = annotation.annotationType();
        
    // method
        Method method = null;
        try {
            method = type.getDeclaredMethod(name);
        } catch (NoSuchMethodException exception) {
            return null;
        } catch (SecurityException exception) {
            return null;
        }
        
    // call
        Object value = null;
        try {
            value = method.invoke(annotation);
        } catch (IllegalAccessException exception) {
            return null;
        } catch (IllegalArgumentException exception) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
        
        return value;
    }
}