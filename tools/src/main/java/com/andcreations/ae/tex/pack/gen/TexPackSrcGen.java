package com.andcreations.ae.tex.pack.gen;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.andcreations.ae.tex.pack.gen.resources.R;
import com.andcreations.ae.util.CodeGenMapping;

/**
 * @author Mikolaj Gucki
 */
public class TexPackSrcGen {
    /** */
    private TexPackSrcGenCfg cfg;
    
    /** */
    private String src;
    
    /** */
    public TexPackSrcGen(TexPackSrcGenCfg cfg) {
        this.cfg = cfg;
    }
    
    /** */
    private CodeGenMapping.Template loadTemplate() throws IOException {
        CodeGenMapping mapping = new CodeGenMapping(R.class);
        return mapping.loadTemplate(cfg.getLuaFileName());
    }

    /**
     * Generates the source code from a template.
     *
     * @param template The template.
     * @return The generated source code.
     */
    private String generateSrcCode(CodeGenMapping.Template template) {
        VelocityContext context = new VelocityContext();
        context.put("id",cfg.getId());
        context.put("filename",cfg.getImageFileName());
        context.put("tex",cfg.getResult());
        context.put("subtextures",cfg.getResult().getSubtextures());
        
    // additional template-related context
        for (String key:template.getContext().keySet()) {
            context.put(key,template.getContext().get(key));
        }
        
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"",template.getTemplate());
        
        return writer.toString();
    }    
    
    /** */
    public void run() throws IOException {
        CodeGenMapping.Template template = loadTemplate();
        src = generateSrcCode(template);
    }
    
    /** */
    public String getSrc() {
        return src;
    }    
}