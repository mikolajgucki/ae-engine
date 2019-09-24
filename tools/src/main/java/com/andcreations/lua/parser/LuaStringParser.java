package com.andcreations.lua.parser;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.andcreations.lua.parser.resources.R;
import com.andcreations.lua.runner.LuaErrorException;
import com.andcreations.lua.runner.LuaRunner;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
public class LuaStringParser {
    /** */
    private static final String TEMPLATE_NAME = "lua_string_parser.vm";
    
    /** */
    private LuaRunner luaRunner;
    
    /** */
    public LuaStringParser(LuaRunner luaRunner) {
        this.luaRunner = luaRunner;
    }
    
    /** */
    private String getSrc(String luaString) throws IOException {
    // template
        String template = ResourceLoader.loadAsString(R.class,TEMPLATE_NAME);
        
    // context
        VelocityContext context = new VelocityContext();
        context.put("str",luaString);
        
    // write
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context,writer,"",template);  
        
        return writer.toString();
    }
    
    /** */
    public String parse(String luaString,File tmpFile)
        throws LuaErrorException,IOException {
    // replace
        luaString = luaString.replaceAll("'","\\\\'");
            
    // source
        String src = getSrc(luaString);
        
    // write to the temporary file and run
        String luaResult = null;
        try {
            FileUtils.writeStringToFile(tmpFile,src,"UTF-8");
            luaResult = luaRunner.run(tmpFile);
        } finally {
            tmpFile.delete();
        }        
        
        String[] codes = luaResult.split(",");
        StringBuilder result = new StringBuilder();
        for (String code:codes) {
            int ch;
            try {
                ch = Integer.parseInt(code);
                if (ch < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException exception) {
                throw new IOException(String.format(
                    "Internale error: Invalid code returned %s.",code));
            }
            result.append((char)ch);
        }
        
        return result.toString();
    }
}