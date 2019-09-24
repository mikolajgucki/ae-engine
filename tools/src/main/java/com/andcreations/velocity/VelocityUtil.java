package com.andcreations.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
public class VelocityUtil {
    /**
     * Evaluates a Velocity template.
     *
     * @param template The template.
     * @param context The context.
     * @return The rendered output.
     * @throws IOException on I/O error.
     */
    public static String evaluate(String template,Map<String,Object> context)
        throws IOException {
    // context
        VelocityContext velocityContext = new VelocityContext();
        for (String key:context.keySet()) {
            velocityContext.put(key,context.get(key));
        }
        
    // render
        StringWriter writer = new StringWriter();
        Velocity.evaluate(velocityContext,writer,"",template);

        return writer.toString();       
    }
    
    /**
     * Evaluates a Velocity template.
     *
     * @param resourcesClass The resources class used to look for the template
     *   resource.
     * @param resourceName The name of the resource with template.
     * @param context The context.
     * @return The rendered output.
     * @throws IOException on I/O error.
     */
    public static String evaluate(Class<?> resourcesClass,String resourceName,
        Map<String,Object> context) throws IOException {
    // template
        String template = ResourceLoader.loadAsString(
            resourcesClass,resourceName);
        
    // evaluate
        return evaluate(template,context);
    }
}