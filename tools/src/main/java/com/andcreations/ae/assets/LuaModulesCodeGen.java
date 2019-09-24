package com.andcreations.ae.assets;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.assets.resources.R;
import com.andcreations.velocity.VelocityUtil;

/**
 * @author Mikolaj Gucki
 */
class LuaModulesCodeGen {
    /** */
    String generate(String localName,List<String> modules,String brief)
        throws IOException {
    // context
        Map<String,Object> context = new HashMap<String,Object>();
        context.put("localName",localName);
        context.put("modules",modules);
        context.put("brief",brief);
 
    // render
        return VelocityUtil.evaluate(R.class,"lua_modules.vm",context);
    }
    
    /** */
    void write(String localName,List<String> modules,String brief,
        LuaModulesCodeGenListener listener,File dstFile) {
    // source
        String src = null;
        try {
            src = generate(localName,modules,brief);
        } catch (IOException exception) {
            listener.failedToGenLuaModulesCode(dstFile,exception);
            return;
        }
        
    // directory
        File dstDir = dstFile.getParentFile();
        if (dstDir.exists() == false) {
            dstDir.mkdirs();
            listener.luaModuleCodeGenDirCreated(dstDir);
        }
        
    // write
        try {
            FileUtils.writeStringToFile(dstFile,src,"UTF-8");
            listener.luaModuleCodeGenFileCreated(dstFile);
        } catch (IOException exception) {
            listener.failedToGenLuaModulesCode(dstFile,exception);
            return;
        }
        
    // notify
        listener.generatedLuaModuleCode(dstFile);
    }   
}