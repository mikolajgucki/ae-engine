package com.andcreations.ae.studio.plugins.lua.wizards;

import com.andcreations.ae.studio.plugins.wizards.Wizards;

/**
 * @author Mikolaj Gucki
 */
public class LuaWizards {
    /** */
    private static LuaWizards instance;
    
    /** */
    void init() {
        Wizards.get().addWizard(new CreateLuaFileWizard());
    }
    
    /** */
    public static LuaWizards get() {
        if (instance == null) {
            instance = new LuaWizards();
        }
        
        return instance;
    }
}