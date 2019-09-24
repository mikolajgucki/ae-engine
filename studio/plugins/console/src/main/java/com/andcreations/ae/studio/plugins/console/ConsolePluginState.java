package com.andcreations.ae.studio.plugins.console;

import java.util.HashMap;
import java.util.Map;

import com.andcreations.ae.studio.plugin.PluginState;

/**
 * @author Mikolaj Gucki
 */
class ConsolePluginState extends PluginState {
    /** */
    private Map<String,ConsoleCfg> cfgMap = new HashMap<>();
    
    /** */
    private int idSeq;
    
    /** */
    private String nextId(ConsoleCfg cfg) {
        idSeq++;
        return cfg.getTitle() + ":" + idSeq;
    }

    /** */
    String addCfg(ConsoleCfg cfg) {
        String id = nextId(cfg);
        cfgMap.put(id,cfg);
        return id;
    }
    
    /** */
    ConsoleCfg getCfg(String id) {
        return cfgMap.get(id);
    }
}