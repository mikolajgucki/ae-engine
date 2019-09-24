package com.andcreations.ae.studio.plugins.lua.debug;

import com.andcreations.ae.studio.plugins.lua.debug.globals.Globals;
import com.andcreations.ae.studio.plugins.lua.debug.snippet.LuaSnippet;
import com.andcreations.ae.studio.plugins.lua.debug.traceback.Traceback;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;

/**
 * @author Mikolaj Gucki
 */
public class LuaDebug {
    /** */
    private static LuaDebug instance;
    
    /** */
    private LuaDebugMenu menu;
    
    /** */
    private LuaDebugSession session;
    
    /** */
    private LuaDebug() {
        create();
    }
    
    /** */
    private void create() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                createMenu();
            }
        });
    }
    
    /** */
    private void createMenu() {
        menu = new LuaDebugMenu(new LuaDebugMenuListener() {
            /** */
            @Override
            public void stepInto() {
                session.stepInto();
            }
            
            /** */
            @Override
            public void stepOver() {
                session.stepOver();
            }
            
            /** */
            @Override
            public void stepReturn() {
                session.stepReturn();
            }
            
            /** */
            @Override
            public void continueExecution() {
                session.continueExecution();
            }
        });
    }
    
    /** */
    void init() {
        detachSession(null);
    }
    
    /** */
    private void clear() {
        Traceback.get().clear();
        Globals.get().clear();        
    }
    
    /** */
    public void attachSession(LuaDebugSession session) {
        this.session = session;
        menu.sessionAttached();
        LuaSnippet.get().sessionAttached();
    }
    
    /** */
    public void detachSession(LuaDebugSession session) {
        clear();
        menu.sessionDetached();
        LuaSnippet.get().sessionDetached();
        this.session = null;
    }
    
    /** */
    public void setSuspended(boolean suspended) {
        menu.setSuspended(suspended);
        if (suspended == false) {
            clear();
        }
    }
    
    /** */
    void runLuaSnippet(String code) {
        if (session != null) {
            session.runLuaSnippet(code);
        }
    }
    
    /** */
    public static LuaDebug get() {
        if (instance == null) {
            instance = new LuaDebug();
        }
        
        return instance;
    }
}