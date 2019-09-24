package com.andcreations.ae.studio.plugins.text.editor;

import java.util.HashMap;
import java.util.Map;

import com.andcreations.ae.studio.plugin.PluginState;

/**
 * The text editor plugin state.
 * 
 * @author Mikolaj Gucki
 */
public class TextEditorState extends PluginState {
    /** */
    private Map<String,EditorCfg> cfgMap = new HashMap<>();
    
    /** */
    void init() {
        for (EditorCfg cfg:cfgMap.values()) {
            cfg.init();
        }
    }
    
    /** */
    void addCfg(EditorCfg cfg) {
        cfgMap.put(cfg.getId(),cfg);
    }
    
    /** */
    EditorCfg getCfg(String id) {
        return cfgMap.get(id);
    }
}
