package com.andcreations.ae.studio.plugins.lua.autocompletion;

import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;

/**
 * @author Mikolaj Gucki
 */
class ShorthandAutocompletion {
    /** */
    private static ShorthandAutocompletion instance;
    
    /** */
    private ShorthandAutocompletion() {
    }
    
    /** */
    void install(EditorMediator editor) {
        ShorthandSource source = new ShorthandSource();
        editor.getAutocompletion().addSource(source.convert());
    }
    
    /** */
    static ShorthandAutocompletion get() {
        if (instance == null) {
            instance = new ShorthandAutocompletion();
        }
        
        return instance;
    }
}