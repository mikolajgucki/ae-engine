package com.andcreations.ae.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.andcreations.lua.LuaTypes;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;

/**
 * Maps the target source file name onto its corresponding Velocity template.
 *
 * @author Mikolaj Gucki
 */
public class CodeGenMapping {
    /** Represents a template from which the source is generated. */
    public static class Template {
        /** The template. */
        private String template;
        
        /** The additional Velocity context. */
        private Map<String,Object> context;
        
        /** */
        private Template(String template,Map<String,Object> context) {
            this.template = template;
            this.context = context;
        }
        
        /**
         * Gets the template.
         *
         * @return The template.
         */
        public String getTemplate() {
            return template;
        }
        
        /**
         * Gets the additional Velocity context.
         *
         * @return The additional context.
         */
        public Map<String,Object> getContext() {
            return context;
        }
    }
    
    /** The Lua source file suffix. */
    private static final String LUA_SUFFIX = "lua";
    
    /** The class related to which load the resources. */
    private Class<?> clazz;    
    
    /** The resources with the suffix-to-template mapping. */
    private BundleResources res;
    
    /** */
    public CodeGenMapping(Class<?> clazz) {
        this.clazz = clazz;
        this.res = new BundleResources(
            clazz.getPackage().getName(),"code_gen_mapping");
    }
    
    /**
     * Checks if the mapping contains the mapping for given file name.
     *
     * @param fileName The file name.
     * @return <code>true</code> if contains, <code>false</code> otherwise.
     */
    public boolean containsMapping(String fileName) {
        String suffix = FilenameUtils.getExtension(fileName);
        return res.containsKey(suffix);
    }
    
    /**
     * Gets the additional context for the given suffix.
     *
     * @param suffix The file suffix.
     * @return The context.
     */
    private Map<String,Object> getContext(String suffix) {
        Map<String,Object> context = new HashMap<String,Object>();
        
    // Lua
        if (LUA_SUFFIX.equals(suffix)) {
            context.put("LuaTypes",LuaTypes.class);
        }
        
        return context;
    }
    
    /**
     * Loads the template for given file name.
     *
     * @param fileName The file name.
     * @return The loaded template or <code>null</code> if there is no template
     *     matching the given file name.
     * @throws IOException on I/O error.
     */
    public Template loadTemplate(String fileName) throws IOException {
    // get template name
        String suffix = FilenameUtils.getExtension(fileName);
        if (res.containsKey(suffix) == false) {
            return null;
        }
        String templateName = res.getStr(suffix);
        
    // load
        ResourceLoader loader = new ResourceLoader(clazz);
        StringBuffer buffer = loader.loadAsStringBuffer(templateName);
        
        return new Template(buffer.toString(),getContext(suffix));
    }
}