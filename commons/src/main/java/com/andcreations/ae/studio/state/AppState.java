package com.andcreations.ae.studio.state;

import java.io.File;

import com.andcreations.io.json.JSON;
import com.andcreations.io.json.JSONException;

/**
 * @author Mikolaj Gucki
 */
public class AppState {
    /** */
    private static AppState instance;
    
    /** */
    private static final String STATE_DIR = ".ae.studio";
        
    /** */
    private static final String STATE_FILE = "state";

    /** */
    private File getAppStateDir() {
        File userHome = new File(System.getProperty("user.home")); 
        return new File(userHome,STATE_DIR);
    }
    
    /** */
    private File getStateDir(Class<?> clazz) {
        File stateDir = new File(getAppStateDir(),clazz.getName());
        if (stateDir.exists() == false) {
            stateDir.mkdirs();
        }
        
        return stateDir;        
    }
    
    /** */
    private File getStateFile(Class<?> clazz) {
        File stateDir = getStateDir(clazz);
        return new File(stateDir,STATE_FILE);
    }
    
    /** */
    public void storeState(Class<?> clazz,Object value) throws JSONException {
        JSON.write(getStateFile(clazz),value);
    }
    
    /** */
    public <T> T restoreState(Class<T> clazz) throws JSONException {
        File file = getStateFile(clazz);
        if (file.exists() == false) {
            return null;
        }
        return JSON.read(file,clazz);
    }
    
    /** */
    public static AppState get() {
        if (instance == null) {
            instance = new AppState();
        }
        
        return instance;
    }    
}