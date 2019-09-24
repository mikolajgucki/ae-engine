package com.andcreations.ae.tex.font.gen;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.andcreations.ae.tex.font.gen.resources.R;
import com.andcreations.ae.util.CodeGenMapping;

/**
 * @author Mikolaj Gucki
 */
public class TexFontSrcGen {
    /** */
    private TexFontSrcGenCfg cfg;
    
    /** */
    private String src;
    
    /** */
    public TexFontSrcGen(TexFontSrcGenCfg cfg) {
        this.cfg = cfg;
    }
    
    /** */
    private CodeGenMapping.Template loadTemplate() throws IOException {
        CodeGenMapping mapping = new CodeGenMapping(R.class);
        return mapping.loadTemplate(cfg.getFileName());
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
        context.put("data",cfg.getResult());
        
    // additional template-related context
        for (String key:template.getContext().keySet()) {
            context.put(key,template.getContext().get(key));
        }
        
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"src.gen",template.getTemplate());
        
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