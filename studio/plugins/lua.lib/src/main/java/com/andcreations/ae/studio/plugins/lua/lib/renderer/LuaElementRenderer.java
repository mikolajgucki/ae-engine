package com.andcreations.ae.studio.plugins.lua.lib.renderer;

import com.andcreations.ae.studio.plugins.lua.lib.parser.Assignment;
import com.andcreations.ae.studio.plugins.lua.lib.parser.Func;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class LuaElementRenderer {
    /** */
    private static final BundleResources res =
        new BundleResources(LuaElementRenderer.class);      
    
    /** */ 
    private static String dark;
    
    /** */
    public static void init() {
        dark = UIColors.toHex(UIColors.dark());
    }
    
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
    public static String assignmentToSearch(Assignment assignment) {
        return assignment.toString();
    }
    
    /** */
    public static String assignmentToPlain(Assignment assignment) {
        String str = assignment.toString(); 
        return res.getStr("assignment.plain",getPrefix(str),getName(str));
    }
    
    /** */
    public static String assignmentToHTML(Assignment assignment) {
        String str = assignment.toString();      
        return res.getStr("assignment.html",getPrefix(str),getName(str),dark);
    }
    
    /** */
    public static String localAssignmentToPlain(Assignment assignment) {
        String str = assignment.toString(); 
        return res.getStr("local.assignment.plain",getPrefix(str),getName(str));
    }
    
    /** */
    public static String localAssignmentToHTML(Assignment assignment) {
        String str = assignment.toString();      
        return res.getStr("local.assignment.html",
            getPrefix(str),getName(str),dark);
    }

    /** */
    public static String funcToSearch(Func func) {
        return func.getName();
    }
    
    /** */
    public static String funcToPlain(Func func) {
        String str = func.getName();
        return res.getStr("func.plain",getPrefix(str),getName(str),
            func.getParams().toString());        
    }    
    
    /** */
    public static String funcToHTML(Func func) {
        String str = func.getName();
        return res.getStr("func.html",getPrefix(str),getName(str),
            func.getParams().toString(),dark);           
    }    
    
    /** */
    public static String localFuncToPlain(Func func) {
        String str = func.getName();
        return res.getStr("local.func.plain",getPrefix(str),getName(str),
            func.getParams().toString());        
    }    
    
    /** */
    public static String localFuncToHTML(Func func) {
        String str = func.getName();
        return res.getStr("local.func.html",getPrefix(str),getName(str),
            func.getParams().toString(),dark);           
    }     
}