package com.andcreations.ae.studio.plugins.simulator;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;

import com.andcreations.ae.desktop.DesktopEngine;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.debug.LuaDebug;
import com.andcreations.ae.studio.plugins.lua.debug.LuaDebugSession;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.text.editor.LineHighlight;
import com.andcreations.ae.studio.plugins.text.editor.LineHighlights;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class SimulatorDebug implements LuaDebugSession {    
    /** */
    private BundleResources res = new BundleResources(SimulatorDebug.class);
    
    /** */
    private DesktopEngine engine;
    
    /** */
    private LineHighlight lineHighlight;
    
    /** */
    private Color highlightColor;
    
    /** */
    private boolean stopping;
    
    /** */
    SimulatorDebug(DesktopEngine engine) {
        this.engine = engine;
        create();
    }
    
    /** */
    private void create() {
        highlightColor = UIColors.blend( 
            UIManager.getColor("Panel.background"),
            UIColors.fromHex("3da0eb"),0.9);
    }
    
    /** */
    private void handleException(String message,Exception exception) {
        Simulator.handleException(message,exception);
    }
    
    /** */
    void starting() {        
        stopping = false;
        clearLineHighlight();
        LuaDebug.get().attachSession(this);
    }
    
    /** */
    void restarting() {
        clearLineHighlight();
    }
    
    /** */
    void stopping() {
        setSuspended(false);
        stopping = true;
    }
    
    /** */
    void stopped() {
        clearLineHighlight();
        LuaDebug.get().detachSession(this);
    }
    
    /** */
    private void setLineHighlight(File file,int line) {
        lineHighlight = LineHighlights.get().addLineHighlight(
            file,line,highlightColor);
    }
    
    /** */
    private void clearLineHighlight() {
        if (lineHighlight != null) {
            LineHighlights.get().removeLineHighlight(lineHighlight);
            lineHighlight = null;
        } 
    }
    
    /** */
    private void setSuspended(final boolean suspended) {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                clearLineHighlight();                        
                LuaDebug.get().setSuspended(suspended);
            }
        });
    }
    
    /** */
    private void suspended(File file,int line) {
    // go to the file
        LuaFile.edit(file,line,line + 8);
        setLineHighlight(file,line);
        
    // globals
        try {
            engine.getGlobals();
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.get.globals",
                exception.getMessage()),exception);
            return;
        }
        
    // traceback
        try {
            engine.getTraceback();
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.get.traceback",
                exception.getMessage()),exception);
            return;
        }
    }
    
    /** */
    void suspended(String source,final int line) {
        setSuspended(true);
        if (stopping == true) {
            return;
        }
        
        String path = "/" + source;
        final File file = ProjectLuaFiles.get().getFileByPath(path);
        if (file != null) {
            UICommon.invokeAndWait(new Runnable() {
                /** */
                @Override
                public void run() {
                    suspended(file,line);
                }
            });
        }
    }
    
    /** */
    @Override
    public void stepInto() {
        setSuspended(false);        
        try {
            engine.stepInto();
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.step.into",
                exception.getMessage()),exception);
            return;
        }
    }
    
    /** */
    @Override
    public void stepOver() {
        setSuspended(false);        
        try {
            engine.stepOver();
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.step.over",
                exception.getMessage()),exception);
            return;
        }        
    }
    
    /** */
    @Override
    public void stepReturn() {
        setSuspended(false);        
        try {
            engine.stepReturn();
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.step.return",
                exception.getMessage()),exception);
            return;
        }        
    }
    
    /** */
    @Override
    public void continueExecution() {
        setSuspended(false);        
        try {
            engine.continueExecution();
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.continue.execution",
                exception.getMessage()),exception);
            return;
        }        
    }
    
    /** */
    @Override
    public void runLuaSnippet(String code) {
        try {
            engine.doLuaString(code);
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.run.lua.snippet",
                exception.getMessage()),exception);
            return;
        }         
    }
}