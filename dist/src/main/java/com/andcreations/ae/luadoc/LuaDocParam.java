package com.andcreations.ae.luadoc;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Represents a parameter with its name and description.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocParam {
    /** The name of the parameter tag. */
    public static final String TAG = "param";
    
    /** The string resources. */
    private static StrResources res = new BundleResources(LuaDocParam.class);        
    
    /** The parameter name. */
    private String name;
    
    /** The parameter description. */
    private String desc;
    
    /**
     * Constructs a {@link LuaDocParam}.
     *
     * @param name The parameter name.
     * @param desc The parameter description.
     */
    public LuaDocParam(String name,String desc) {
        this.name = name;
        this.desc = desc;
    }
    
    /**
     * Gets the parameter name.
     *
     * @return The parameter name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the parameter description.
     * 
     * @return The parameter description.
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * Parses a parameter from a tag.
     *
     * @param tag The tag.
     * @return The parsed parameter.
     * @throws LuaDocException if the parameter cannot be parsed.
     */
    public static LuaDocParam parse(LuaDocTag tag) throws LuaDocException {
        if (TAG.equals(tag.getName()) == false) {
            throw new LuaDocException(res.getStr("not.a.param"));
        }
        
        String tagValue = tag.getValue();
    // check it has name
        int indexOf = tagValue.indexOf(' ');
        if (indexOf <= 0) {
            throw new LuaDocException(res.getStr("no.param.name"));
        }        
        
        String paramName = tagValue.substring(0,indexOf);
        String paramValue = tagValue.substring(indexOf + 1);
        
    // markdown
        try {
            paramValue = new Markdown4jProcessor().process(paramValue);
        } catch (IOException exception) {
            throw new LuaDocException(exception.getMessage(),exception);
        }
        
        return new LuaDocParam(paramName,paramValue);
    }
}