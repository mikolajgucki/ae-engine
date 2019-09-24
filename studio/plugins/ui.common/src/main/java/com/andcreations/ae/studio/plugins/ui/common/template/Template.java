package com.andcreations.ae.studio.plugins.ui.common.template;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
public class Template {
    /** */
    private static final BundleResources res =
        new BundleResources(Template.class); 
    
    /** */
    public static String evaluate(Class<?> clazz,String templateName,
        VelocityContext context) {
    //        
        String template;
        try {
            template = ResourceLoader.loadAsString(clazz,templateName);
        } catch (IOException exception) {
            Log.error(res.getStr("failed.to.load.template",templateName,
                exception.getMessage()));
            return null;
        }
        
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"src.gen",template);        
        
        return writer.toString();
    }
}