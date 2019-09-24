package com.andcreations.ae.studio.plugins.text.editor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.andcreations.ae.studio.plugins.ui.main.view.View;

/**
 * Caches the currently opened views.
 *
 * @author Mikolaj Gucki
 */
class TextEditorCache {
    /** */
    private Map<EditorCfg,View> viewMap = new HashMap<>();
    
    /** */
    synchronized void put(EditorCfg cfg,View view) {
        viewMap.put(cfg,view);
    }
    
    /** */
    synchronized View get(EditorCfg cfg) {
        File cfgFile = new File(cfg.getFilePath());
        
        for (EditorCfg key:viewMap.keySet()) {
            File keyFile = new File(key.getFilePath());
            if (keyFile.equals(cfgFile) == true) {
                return viewMap.get(key);
            }
        }
        
        return null;
    }
    
    /** */
    synchronized void remove(EditorCfg cfg) {
        viewMap.remove(cfg);    
    }        
}