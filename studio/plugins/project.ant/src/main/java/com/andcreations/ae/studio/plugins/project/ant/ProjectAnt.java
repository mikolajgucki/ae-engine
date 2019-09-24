package com.andcreations.ae.studio.plugins.project.ant;

import com.andcreations.ae.studio.plugins.ant.Ant;
import com.andcreations.ae.studio.plugins.ant.AntPreferences;
import com.andcreations.ae.studio.plugins.ant.AntPreferencesAdapter;

/**
 * @author Mikolaj Gucki
 */
public class ProjectAnt {
    /** */
    private static ProjectAnt instance;
    
    /** */
    void init() {
        AntPreferences.get().addAntPreferencesListener(
            new AntPreferencesAdapter() {
                /** */
                @Override
                public void antHomeChanged(String oldAntHome,
                    String newAntHome) {
                //
                    tryCreateBuilders();
                }
            });
        
        tryCreateBuilders();
    }
    
    /** */
    private void tryCreateBuilders() {
        if (Ant.get().isAntAvailable() == true) {
            ProjectAntBuilders.get().createBuilders();
        }
        else {
            ProjectAntBuilders.get().deleteBuilders();
        }
    }
    
    /** */
    public static ProjectAnt get() {
        if (instance == null) {
            instance = new ProjectAnt();
        }
        
        return instance;
    }
}