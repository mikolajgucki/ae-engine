package com.andcreations.ae.studio.plugins.lua.parser.renderer;

import com.andcreations.ae.lua.parser.LuaFunc;
import com.andcreations.ae.lua.parser.LuaVariableExpression;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class LuaElementRenderer {
    /** */
    public static final String SEPARATOR = ",";
    
    /** */
    private static final BundleResources res =
        new BundleResources(LuaElementRenderer.class);      
    
    /** */
    private static String getPrefix(String str) {
        int indexOf = str.lastIndexOf(":");
        if (indexOf < 0) {
            indexOf = str.lastIndexOf(".");
        }
        
        if (indexOf > 0) {
            return str.substring(0,indexOf);
        }
        return "";
    }
    
    /** */
    private static String getName(String str) {
        int indexOf = str.lastIndexOf(":");
        if (indexOf < 0) {
            indexOf = str.lastIndexOf(".");
        }
        
        if (indexOf > 0) {
            return str.substring(indexOf,str.length());
        }
        return str;
    }     
    
    /** */
    public static String elementToPlain(String elementName,String details) {
        String prefix = getPrefix(elementName);
        String name = getName(elementName);
        
        return res.getStr("element.plain",prefix,name,details);
    }   
    
    /** */
    public static String elementToHTML(String elementName,String details) {
        String prefix = getPrefix(elementName);
        String name = getName(elementName);
        
        return res.getStr("element.html",prefix,name,details,
            UIColors.htmlDark());
    }      
    
    /** */
    public static String elementToSearch(String elementName) {
        return elementName;
    }
        
    /** */
    public static String expressionToPlain(LuaVariableExpression exp,
        String details) {
    //
        String prefix = getPrefix(exp.toString());
        String name = getName(exp.toString());
        
        return res.getStr("var.exp.plain",prefix,name,details);
    }
        
    /** */
    public static String expressionToHTML(LuaVariableExpression exp,
        String details) {
    //
        String prefix = getPrefix(exp.toString());
        String name = getName(exp.toString());
        
        return res.getStr("var.exp.html",prefix,name,details,
            UIColors.htmlDark());
    }
    
    /** */
    public static String expressionToSearch(LuaVariableExpression exp) {
        return exp.toString();
    }
    
    /** */
    public static String funcToPlain(LuaFunc func) {
        String prefix = getPrefix(func.getName());
        String name = getName(func.getName());
        String params = func.getParams().toString();
        
        return res.getStr("func.plain",prefix,name,params);
    }    
    
    /** */
    public static String funcToHTML(LuaFunc func) {
        String prefix = getPrefix(func.getName());
        String name = getName(func.getName());
        String params = func.getParams().toString();
        
        return res.getStr("func.html",prefix,name,params,UIColors.htmlDark());
    }    
    
    /** */
    public static String funcToSearch(LuaFunc func) {
        return funcToPlain(func);
    }
    
    /** */
    public static String localFuncToPlain(LuaFunc func) {
        String prefix = getPrefix(func.getName());
        String name = getName(func.getName());
        String params = func.getParams().toString();
        
        return res.getStr("local.func.plain",prefix,name,params);
    }    
    
    /** */
    public static String localFuncToHTML(LuaFunc func) {
        String prefix = getPrefix(func.getName());
        String name = getName(func.getName());
        String params = func.getParams().toString();
        
        return res.getStr("local.func.html",prefix,name,params,
            UIColors.htmlDark());
    }     
}