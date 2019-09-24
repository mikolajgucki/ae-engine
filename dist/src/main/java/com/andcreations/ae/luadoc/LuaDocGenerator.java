package com.andcreations.ae.luadoc;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.andcreations.ae.luadoc.resources.R;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;
import com.andcreations.resources.StrResources;

/**
 * Generates documents from LuaDoc modules.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocGenerator {
    /** The string resources. */
    private StrResources res = new BundleResources(LuaDocGenerator.class);
    
    /** The configuration. */
    private LuaDocGeneratorCfg cfg;
    
    /**
     * Constructs a {@link LuaDocGenerator}.
     *
     * @param cfg The configuration.
     */
    public LuaDocGenerator(LuaDocGeneratorCfg cfg) {
        this.cfg = cfg;
    }
    
    /**
     * Loads a template.
     *
     * @param name The template name.
     * @return The loaded template.
     * @throws LuaDocException if the template cannot be loaded.
     */
    private String loadDocTemplate(String name) throws LuaDocException {
        ResourceLoader loader = new ResourceLoader(R.class);
        
        StringBuffer buffer;
        try {
            buffer = loader.loadAsStringBuffer(name);
        } catch (IOException exception) {
            throw new LuaDocException(res.getStr("failed.to.load.template",
                name,exception.getMessage()),exception);
        }
        
        return buffer.toString();
    }
    
    /**
     * Generates the navigation.
     *
     * @param data The parsed data.
     * @return The generated navigation.
     * @throws LuaDocException if the generation fails.
     */
    private String generateNavigation(LuaDocData data)
        throws LuaDocException {
    //
        VelocityContext context = new VelocityContext();
        context.put("title",cfg.getTitle());        
        context.put("modules",data.getModules());
        context.put("groups",data.getGroups());
        
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"luadoc",
            loadDocTemplate("navigation.vm"));
        
        return writer.toString();     
    }

    /**
     * Generates a document from a template.
     *
     * @param module The module.
     * @param data The parsed data.
     * @param templateName The template name.
     * @return The module document.
     * @param LuaDocException if the document generation fails.
     */
    private String generateDoc(LuaDocModule module,LuaDocData data,
        String templateName) throws LuaDocException {
    //
        VelocityContext context = new VelocityContext();
        context.put("data",data);
        context.put("title",cfg.getTitle());        
        context.put("module",module);
        context.put("modules",data.getModules());
        context.put("groups",data.getGroups());
        context.put("navigation",generateNavigation(data));
        
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"luadoc",
            loadDocTemplate(templateName));
        
        return writer.toString();        
    }    
    
    /**
     * Generates a document from a module.
     *
     * @param module The module.
     * @param data The parsed data.
     * @return The module document.
     * @param LuaDocException if the document generation fails.
     */
    public String generateModuleDoc(LuaDocModule module,
        LuaDocData data) throws LuaDocException {
    //
        return generateDoc(module,data,"module.vm");    
    }    
    
    /**
     * Generates the index document.
     *
     * @param data The parsed data.
     * @return The index document.
     * @param LuaDocException if the document generation fails.
     */
    public String generateIndexDoc(LuaDocData data)
        throws LuaDocException {
    //
        return generateDoc(null,data,"index.vm");        
    }
    
    /**
     * Copies a resource to a directory.
     *
     * @param dstDir The directory to which to copy.
     * @param name The name of the resource to copy.
     * @throws IOException on I/O error.
     */
    private static void copyResource(File dstDir,String name)
        throws IOException {
    //
        ResourceLoader loader = new ResourceLoader(R.class);
        loader.copyResource(dstDir,name);
    }
    
    /**
     * Copies the necessary files to a directory.
     *
     * @param dstDir The directory to which to copy.
     * @throws IOException on I/O error.
     */
    public static void copyFiles(File dstDir) throws IOException {
        copyResource(dstDir,"luadoc.css");
    }
}