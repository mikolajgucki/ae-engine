package com.andcreations.ae.lua.doc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocFunc implements LuaDocNamedElement {
    /** */
    public static final String ERROR_NAME_TAG_NOT_FIRST = "name.tag.not.first";
    
    /** */
    public static final String NOT_FOLLOWED_BY_FUNC = "not.followed.by.func";
    
    /** */
    public static final String FUNC_WITHOUT_NAME = "func.without.name";
    
    /** */
    private String definedIn;
    
    /** */
    private String name;
    
    /** */
    private String localName;
    
    /** */
    private String globalName;
    
    /** */
    private boolean local;
    
    /** */
    private boolean method;
    
    /** */
    private List<LuaDocFuncVariant> variants = new ArrayList<>();
   
    /** */
    LuaDocFunc() {
    }
    
    /** */
    public void setDefinedIn(String definedIn) {
        this.definedIn = definedIn;
    }
    
    /** */
    public String getDefinedIn() {
        return definedIn;
    }
    
    /** */
    public void setName(String name) {
        this.name = name;
    }
    
    /** */
    @Override    
    public String getName() {
        return name;
    }
    
    /** */
    public void setLocalName(String localName) {
        this.localName = localName;
    }
    
    /** */
    @Override
    public String getLocalName() {
        return localName;
    }    
    
    /** */
    @Override
    public String getGlobalName() {
        return globalName;
    }    

    /** */
    public void setLocal(boolean local) {
        this.local = local;
    }
    
    /** */
    public boolean isLocal() {
        return local;
    }

    /** */
    public void setMethod(boolean method) {
        this.method = method;
    }
    
    /** */
    public boolean isMethod() {
        return method;
    }
    
    /** */
    public void addVariant(LuaDocFuncVariant variant) {
        variants.add(variant);
    }
    
    /** */
    public List<LuaDocFuncVariant> getVariants() {
        return Collections.unmodifiableList(variants);
    }
    
    /** */
    void updateGlobalName(String moduleName) {
        if (local == true) {
            return;
        }
        
        if (localName == null) {
            globalName = name;
            return;
        }
        
        String separator = method ? ":" : ".";
        globalName = moduleName + separator + localName;
    }
}