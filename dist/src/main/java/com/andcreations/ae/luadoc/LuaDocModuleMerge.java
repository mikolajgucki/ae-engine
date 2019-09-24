package com.andcreations.ae.luadoc;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Merges modules of the same name.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocModuleMerge {
    /** The string resources. */
    private static StrResources res =
        new BundleResources(LuaDocModuleMerge.class);  
    
    /**
     * Returns the non-null string out of two.
     *
     * @param sa The first string.
     * @param sb The second string.
     * @return The non-null string.
     * @throws LuaDocException if both of the strings are non-null.
     */
    private static String merge(String sa,String sb) throws LuaDocException {
        if (sa != null && sb != null) {
            throw new LuaDocException();
        }
        
        return sa != null ?  sa : sb;
    }
        
    /**
     * Merges two modules.
     *
     * @param ma The first module which is result module.
     * @param mb The second module.
     * @throws LuaDocException if the modules cannot be merged.
     */
    private static void mergeModules(LuaDocModule ma,LuaDocModule mb)
        throws LuaDocException {
    //
        if (StringUtils.equals(ma.getGroup(),mb.getGroup()) == false) {
            throw new LuaDocException(res.getStr("different.groups",
                ma.getName(),ma.getGroup(),mb.getGroup(),
                ma.getSrcFilename(),mb.getSrcFilename()));
        }
        
    // brief
        try {
            ma.setBriefDesc(merge(ma.getBriefDesc(),mb.getBriefDesc()));
        } catch (LuaDocException exception) {
            throw new LuaDocException(res.getStr("two.briefs",
                ma.getName(),ma.getSrcFilename(),mb.getSrcFilename()));
        }
        
    // full
        try {
            ma.setFullDesc(merge(ma.getFullDesc(),mb.getFullDesc()));
        } catch (LuaDocException exception) {
            throw new LuaDocException(res.getStr("two.fulls",
                ma.getName(),ma.getSrcFilename(),mb.getSrcFilename()));
        }
        
    // supermodule
        try {
            ma.setSuperModuleName(
                merge(ma.getSuperModuleName(),mb.getSuperModuleName()));
        } catch (LuaDocException exception) {
            throw new LuaDocException(res.getStr("two.supermodules",
                ma.getName(),ma.getSrcFilename(),mb.getSrcFilename()));
        }
        
    // variables, functions
        ma.getVars().addAll(mb.getVars());
        ma.getFuncs().addAll(mb.getFuncs());
    }
    
    /** 
     * Merges modules of the same name.
     *
     * @param modules The modules.
     * @throws LuaDocException if there modules which cannot be merged.
     */
    public static void merge(List<LuaDocModule> modules)
        throws LuaDocException {
    //
        while (true) {
            LuaDocModule ma = null;
            LuaDocModule mb = null;
            
            boolean found = false;            
            FindLoop:
            for (int ia = 0; ia < modules.size(); ia++) {
                ma = modules.get(ia);
                for (int ib = ia + 1; ib < modules.size(); ib++) {
                    mb = modules.get(ib);
                    
                    if (ma.getName().equals(mb.getName())) {
                        found = true;
                        break FindLoop;
                    }
                }
            }
            
            if (found == false) {
                break;
            }
            mergeModules(ma,mb);
            modules.remove(mb);            
        }
    }    
}