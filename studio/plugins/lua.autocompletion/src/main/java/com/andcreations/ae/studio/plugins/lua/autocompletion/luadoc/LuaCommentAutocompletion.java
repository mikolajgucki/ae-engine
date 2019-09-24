package com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc;

import com.andcreations.ae.lua.doc.LuaDocTag;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.AutocomplSource;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.BasicAutocompl;
import com.andcreations.resources.BundleResources;

/**
 * Provides the LuaDoc from the Lua comments.
 *
 * @author Mikolaj Gucki
 */
public class LuaCommentAutocompletion {
    /** */
    private static LuaCommentAutocompletion instance;
    
    /** */
    private final BundleResources res =
        new BundleResources(LuaCommentAutocompletion.class);       
    
    /** */
    private AutocomplSource createAutocomplSource() {
        AutocomplSource source = new AutocomplSource();
        
    // for each tag name
        for (String tag:LuaDocTag.NAMES) {
            source.addBasic(new BasicAutocompl("@" + tag,res.getStr(tag)));
        }
        
        return source;
    }
    
    /** */
    public void install(EditorMediator editor) {
        editor.getAutocompletion().addCommentSource(createAutocomplSource());
    }
    
    /** */
    public void init() {
    }
    
    /** */
    public static LuaCommentAutocompletion get() {
        if (instance == null) {
            instance = new LuaCommentAutocompletion();
        }
        
        return instance;
    }    
}