package com.andcreations.ae.studio.plugins.lua.autocompletion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugins.lua.autocompletion.resources.R;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.AutocomplSource;
import com.andcreations.resources.ResourceLoader;
import com.google.gson.Gson;

/**
 * Reads autocompletions from files/resources.
 *
 * @author Mikolaj Gucki
 */
class PredefinedAutocompletion {
    /** */
    private static PredefinedAutocompletion instance;    
    
    /** */
    private List<PredefinedSource> predefinedSources = new ArrayList<>();
    
    /** */
    private PredefinedAutocompletion() {
    }
    
    /** */
    private List<AutocomplSource> convert() {
        List<AutocomplSource> sources = new ArrayList<>();        
        for (PredefinedSource predefinedSource:predefinedSources) {
            sources.add(predefinedSource.convert());
        }
        
        return sources;
    }
    
    /** */
    void install(EditorMediator editor) {
        List<AutocomplSource> sources = convert();
        for (AutocomplSource source:sources) {
            editor.getAutocompletion().addSource(source);
        }
    }
    
    /** */
    private PredefinedSource loadFromStr(String str) throws IOException {
        Gson gson = new Gson();
        PredefinedSource predefinedSource =
            gson.fromJson(str,PredefinedSource.class);        
        return predefinedSource;
    }
    
    /** */
    private void loadFromJson(String name) throws PluginException {
        try {
            String res = ResourceLoader.loadAsString(R.class,name);
            predefinedSources.add(loadFromStr(res));
        } catch (IOException exception) {
            throw new PluginException(String.format(
                "Failed to read predefined autocompletion %s",name));            
        }
    }
    
    /** */
    private void loadBulkFromJson(String... names) throws PluginException {
        for (String name:names) {
            loadFromJson(name + ".json");
        }
    }
    
    /** */
    void init() throws PluginException {
        loadBulkFromJson("basic","string","math","table","os","utf8");
    }
    
    /** */
    static PredefinedAutocompletion get() {
        if (instance == null) {
            instance = new PredefinedAutocompletion();
        }
        
        return instance;
    }
}