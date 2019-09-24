package com.andcreations.ae.studio.plugins.lua.autocompletion;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.FuncAutocompl;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class PredefinedFunc {
    /** */
    static class Param {
        /** */
        private String name;
        
        /** */
        private String desc;
        
        /** */
        void setName(String name) {
            this.name = name;
        }
        
        /** */
        String getName() {
            return name;
        }
        
        /** */
        void setDesc(String desc) {
            this.desc = desc;
        }
        
        /** */
        String getDesc() {
            return desc;
        }
    }
    
    /** */
    static class Variant {
        /** */
        private String prefix;
        
        /** */
        private String args;
        
        /** */
        private String replace;
        
        /** */
        private String desc;
        
        /** */
        private List<Param> params;        
        
        /** */
        String getPrefix() {
            return prefix;
        }
        
        /** */
        void setPrefix(String prefix) {
            this.prefix = prefix;
        }
        
        /** */
        String getArgs() {
            return args;
        }
        
        /** */
        void setArgs(String args) {
            this.args = args;
        }
        
        /** */
        String getReplace() {
            return replace;
        }
        
        /** */
        void setReplace(String replace) {
            this.replace = replace;
        }     
        
        /** */
        void setDesc(String desc) {
            this.desc = desc;
        }
        
        /** */
        String getDesc() {
            return desc;
        }
        
        /** */
        List<Param> getParams() {
            return params;
        }
        
        /** */
        void setParams(List<Param> params) {
            this.params = params;
        }        
    }
    
    /** */
    private static final BundleResources res =
        new BundleResources(PredefinedFunc.class); 
    
    /** */
    private static String dark;
        
    /** */
    private String name;
    
    /** */
    private String desc;
    
    /** */
    private String definedIn;
    
    /** */
    private List<Param> params;
    
    /** */
    private List<Variant> variants;
        
    /** */
    static {
        init();
    }    
    
    /** */
    private static void init() {
        dark = UIColors.toHex(UIColors.dark());
    }
    
    /** */
    String getName() {
        return name;
    }
    
    /** */
    void setName(String name) {
        this.name = name;
    }
    
    /** */
    String getDesc() {
        return desc;
    }
    
    /** */
    void setDesc(String desc) {
        this.desc = desc;
    }
    
    /** */
    String getDefinedIn() {
        return definedIn;
    }
    
    /** */
    void setDefinedIn(String definedIn) {
        this.definedIn = definedIn;
    }
    
    /** */
    List<Param> getParams() {
        return params;
    }
    
    /** */
    void setParams(List<Param> params) {
        this.params = params;
    }
    
    /** */
    List<Variant> getVariants() {
        return variants;
    }
    
    /** */
    void setVariants(List<Variant> variants) {
        this.variants = variants;
    }
    
    /** */
    private String getArgs(List<Param> params) {
        String args = "";
        
        if (params != null) {
            for (Param param:params) {
                if (args.length() > 0) {
                    args += ",";
                }
                args += param.getName();
            }
        }
        
        return args;
    }
    
    /** */
    List<FuncAutocompl> convert() {
        List<FuncAutocompl> funcAutocomplList = new ArrayList<>();
    // for each variant
        for (PredefinedFunc.Variant variant:getVariants()) {
        // prefix
            String prefix = variant.getPrefix();
            if (prefix == null) {
                prefix = "";
            }
            
        // create
            FuncAutocompl funcAutocompl = new FuncAutocompl(
                getName(),prefix);
            funcAutocomplList.add(funcAutocompl);
            
        // parameters
            List<Param> params = variant.getParams();
            if (params == null) {
                params = this.params;
            }            
            String args = getArgs(params);
            
        // details
            funcAutocompl.setIcon(Icons.getIcon(LuaIcons.LUA_FUNC));
            funcAutocompl.setReplacementText(variant.getReplace());
            funcAutocompl.setDefinedIn(getDefinedIn());
            funcAutocompl.setDisplayText(res.getStr("display.text",
                prefix,getName(),args));
            funcAutocompl.setDisplayHTML(res.getStr("display.html",
                prefix,getName(),args,dark));
            funcAutocompl.setDefinitionString(res.getStr("definition.string",
                prefix,getName(),args));

        // description
            String desc = variant.getDesc();
            if (desc == null) {
                desc = getDesc();
            }            
            funcAutocompl.setDescription(desc);            
            
        // parameters
            if (params != null) {
                for (Param param:params) {
                    funcAutocompl.addParam(param.getName(),param.getDesc());
                }
            }
        }
        return funcAutocomplList;
    }    
}