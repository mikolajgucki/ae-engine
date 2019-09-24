package com.andcreations.ae.doc.nav;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.andcreations.ae.doc.nav.resources.R;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
public class AEDocNavGen {
    /** */
    private String loadTemplate() throws IOException {
        ResourceLoader loader = new ResourceLoader(R.class);
        return loader.loadAsString("nav.vm");
    }
    
    /** */
    private String evaluate(String template,List<AEDocNavEntry> entries) {
        VelocityContext context = new VelocityContext();
        context.put("entries",entries);
        
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"nav",template);
        
        return writer.toString();         
    }    
    
    /** */
    public String generate(List<AEDocNavEntry> entries) throws IOException {
        String template = loadTemplate();
        return evaluate(template,entries);
    }
}