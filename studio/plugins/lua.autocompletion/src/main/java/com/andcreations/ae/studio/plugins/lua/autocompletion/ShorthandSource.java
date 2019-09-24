package com.andcreations.ae.studio.plugins.lua.autocompletion;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.text.editor.autocomplete.AutocomplSource;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.ShorthandAutocompl;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class ShorthandSource {
    /** */
    private static final BundleResources res =
        new BundleResources(ShorthandSource.class);     
        
    /** */
    private List<ShorthandAutocompl> shorthands = new ArrayList<>();        
    
    /** */
    ShorthandSource() {
        create();
    }
    
    /** */
    private void create() {
        for (String key:res.getKeys()) {
            String text = key;
            String replacementText = res.getStr(key);
            shorthands.add(new ShorthandAutocompl(text,replacementText));
        }
    }
        
    /** */
    AutocomplSource convert() {
        AutocomplSource autocomplSource = new AutocomplSource();
        for (ShorthandAutocompl shorthand:shorthands) {
            autocomplSource.addShorthand(shorthand);
        }
        
        return autocomplSource;
    }
}