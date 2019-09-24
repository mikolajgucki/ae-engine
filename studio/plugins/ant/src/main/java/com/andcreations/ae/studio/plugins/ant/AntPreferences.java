package com.andcreations.ae.studio.plugins.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.problems.Problem;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.Problems;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.main.preferences.Preferences;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class AntPreferences {
    /** */
    private static AntPreferences instance;
    
    /** */
    private BundleResources res = new BundleResources(AntPreferences.class);      
    
    /** */
    private AntAppState appState;
    
    /** */
    private Problem antHomeProblem;
    
    /** */
    private List<AntPreferencesListener> listeners = new ArrayList<>();
    
    /** */
    void init(final AntAppState appState) {
        this.appState = appState;
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                Preferences.get().addPage(new AntPreferencesPage(appState));
            }
        });

        appState.addAntAppStateListener(new AntAppStateListener() {
            /** */
            @Override
            public void antHomeChanged(String oldAntHome,String newAntHome) {
                AntPreferences.this.antHomeChanged(oldAntHome,newAntHome);
            }
        });
        
        validateAntHome(); 
    }
    
    /** */
    public void addAntPreferencesListener(AntPreferencesListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    private void antHomeChanged(String oldAntHome,String newAntHome) {
        validateAntHome();
        
    // notify
        synchronized (listeners) {
            for (AntPreferencesListener listener:listeners) {
                listener.antHomeChanged(oldAntHome,newAntHome);
            }
        }
    }

    
    /** */
    public File getAntHome() {
        if (appState.getAntHome() == null) {
            return null;
        }
        
        return new File(appState.getAntHome());
    }
    
    /** */
    private void validateAntHome() {        
        if (antHomeProblem != null) {
            Problems.get().removeProblem(antHomeProblem);
        }
        
        File antHome = getAntHome();
   // validate
        String warning = AntHome.validate(antHome);
        if (warning != null) {
            String msg = res.getStr("ant.home.error",warning);            
            antHomeProblem = Problems.get().add(AntPreferences.class.getName(),
                ProblemSeverity.WARNING,msg);
        }    
    }
    
    /** */
    public static AntPreferences get() {
        if (instance == null) {
            instance = new AntPreferences();
        }
        
        return instance;
    }
}