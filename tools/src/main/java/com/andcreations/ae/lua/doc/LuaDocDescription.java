package com.andcreations.ae.lua.doc;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocDescription {
    /** */
    private boolean luaDocLinksEnabled = true;
    
    /** */
    private Markdown4jProcessor markdown4j = new Markdown4jProcessor();
    
    /** */
    public void setLuaDocLinksEnabled(boolean enabled) {
        this.luaDocLinksEnabled = enabled;
    }
    
    /** */
    private String getLuaDocLinkHref(String target) {
        int indexOf = target.lastIndexOf(':');
        if (indexOf == -1) {
            indexOf = target.lastIndexOf('.');
        }
        
        return String.format("%s.html#%s",target.substring(0,indexOf),target);
    }
    
    /** */
    private String processLuaDocLinks(String desc) {
        while (true) {
            int start = desc.indexOf("()[");
            if (start < 0) {
                break;
            }                
            int end = desc.indexOf("]",start);
            if (end < 0) {
                break;
            }
            
            String target = desc.substring(start + 3,end);
            if (luaDocLinksEnabled == true) {
                String href = getLuaDocLinkHref(target);
                desc = String.format("%s[%s](%s)%s",desc.substring(0,start),
                    target,href,desc.substring(end + 1,desc.length()));
            }
            else {
                desc = String.format("%s`%s`%s",desc.substring(0,start),
                    target,desc.substring(end + 1,desc.length()));
            }
        }
        
        return desc;
    }    
    
    /** */
    public String process(String desc) throws IOException {
        if (desc == null) {
            return null;
        }
        return markdown4j.process(processLuaDocLinks(desc));
    }    
    
    /** */
    public void process(LuaDocFileData data) throws IOException {
    // module
        LuaDocModule module = data.getModule();
        if (module != null) {
            module.setBrief(process(module.getBrief()));
        }
        
    // variables
        for (LuaDocVar var:data.getVars()) {
            var.setBrief(process(var.getBrief()));
            var.setFull(process(var.getFull()));
        }
        
    // functions
        for (LuaDocFunc func:data.getFuncs()) {
            if (func.getVariants() != null) {
                for (LuaDocFuncVariant variant:func.getVariants()) {
                    variant.setBrief(process(variant.getBrief()));
                    variant.setFull(process(variant.getFull()));
                    
                    if (variant.getParams() != null) {
                        for (LuaDocFuncParam param:variant.getParams()) {
                            param.setDesc(process(param.getDesc()));
                        }
                    }
                }
            }
        }
    }
    
    /** */
    public void process(LuaDocFileDataList dataList) throws IOException {
        for (LuaDocFileData data:dataList.getDataList()) {
            process(data);
        }
    }
}