package com.andcreations.ae.studio.plugins.android;

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
public class AndroidPreferences {
    /** */
    private static AndroidPreferences instance;
    
    /** */
    private static final String PROBLEM_SOURCE_ID =
        AndroidPreferences.class.getName();    
    
    /** */
    private BundleResources res = new BundleResources(AndroidPreferences.class);    
    
    /** */
    private AndroidAppState appState;
    
    /** */
    private List<AndroidPreferencesListener> listeners = new ArrayList<>();
    
    /** */
    private Problem androidSDKDirProblem;
    
    /** */
    void init(final AndroidAppState appState) {
        this.appState = appState;
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                Preferences.get().addPage(new AndroidPreferencesPage(appState));
            }
        });
        
        appState.addAndroidAppStateListener(new AndroidAppStateListener() {
            /** */
            @Override
            public void androidSDKDirChanged(String oldSDKDir,String newSDKDir) {
                checkSDKDirProblem();
                notifyAndroidSDKDirChanged(oldSDKDir,newSDKDir);
            }
        });
        
        checkSDKDirProblem();
    }
    
    /** */
    public void addAndroidPreferencesListener(
        AndroidPreferencesListener listener) {
    //
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    private void checkSDKDirProblem() {
    // remove problem
        if (androidSDKDirProblem != null) {
            Problems.get().removeProblem(androidSDKDirProblem);
            androidSDKDirProblem = null;
        }
        
        String dir = appState.getAndroidSDKDir();
        if (dir != null) {
            String error = AndroidSDKDir.validate(new File(dir));
        // add problem
            if (error != null) {
                String description = res.getStr("problem.description",error);
                androidSDKDirProblem = new Problem(PROBLEM_SOURCE_ID,
                    ProblemSeverity.WARNING,description);
                androidSDKDirProblem.setResource(dir);
                Problems.get().addProblem(androidSDKDirProblem);
            }          
        }
    }

    /** */
    private void notifyAndroidSDKDirChanged(String oldSDKDir,String newSDKDir) {
        File oldDir = null;
        if (oldSDKDir != null) {
            oldDir = new File(oldSDKDir);
        }
        File newDir = new File(newSDKDir);
        
        synchronized (listeners) {
            for (AndroidPreferencesListener listener:listeners) {
                listener.androidSDKDirChanged(oldDir,newDir);
            }
        }
    }
    
    /** */
    public File getAndroidSDKDir() {
        String dir = appState.getAndroidSDKDir();
        if (dir == null || dir.length() == 0) {
            return null;
        }
        
        return new File(dir);
    }
    
    /** */
    public static AndroidPreferences get() {
        if (instance == null) {
            instance = new AndroidPreferences();
        }
        
        return instance;
    }
}
