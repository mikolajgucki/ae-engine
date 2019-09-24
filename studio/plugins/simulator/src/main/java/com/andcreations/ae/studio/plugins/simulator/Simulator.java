package com.andcreations.ae.studio.plugins.simulator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.desktop.DesktopEngine;
import com.andcreations.ae.desktop.DesktopEngineCfg;
import com.andcreations.ae.desktop.DesktopEngineListener;
import com.andcreations.ae.desktop.LogLevel;
import com.andcreations.ae.desktop.LuaTraceback;
import com.andcreations.ae.desktop.LuaValue;
import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.ae.dist.AEDistPlugin;
import com.andcreations.ae.studio.plugins.lua.compiler.LuaCompiler;
import com.andcreations.ae.studio.plugins.lua.debug.breakpoints.LuaBreakpoint;
import com.andcreations.ae.studio.plugins.lua.debug.breakpoints.LuaBreakpoints;
import com.andcreations.ae.studio.plugins.lua.debug.breakpoints.LuaBreakpointsListener;
import com.andcreations.ae.studio.plugins.lua.debug.snippet.LuaSnippet;
import com.andcreations.ae.studio.plugins.problems.Problems;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.project.ProjectPlugin;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.StatusBar;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDecorator;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Simulator {
    /** */
    private static final String VIEW_ID = "simulator";
    
    /** */
    private static Simulator instance;
    
    /** */
    private BundleResources res = new BundleResources(Simulator.class);
        
    /** */
    private SimulatorState state;
    
    /** */
    private File storageDir;
    
    /** */
    private SimulatorComponent component;
    
    /** */
    private List<SimulatorListener> listeners = new ArrayList<>();
    
    /** */
    private SimulatorMenu menu;
    
    /** */
    private DesktopEngineCfg engineCfg;
    
    /** */
    private DesktopEngine engine;
    
    /** */
    private SimulatorDebug simulatorDebug;
    
    /** */
    private SimulatorTraceback simulatorTraceback;
    
    /** */
    private SimulatorGlobals simulatorGlobals;
    
    /** */
    private boolean paused;
    
    /** */
    private SimulatorStatus status;       
    
    /** */
    private Simulator() {
    }
    
    /** */
    void init(SimulatorState state,File storageDir,AEDistPlugin aeDistPlugin,
        ProjectPlugin projectPlugin) {
    // state, storage
        this.state = state;
        this.storageDir = storageDir;
    
    // menu        
        menu = new SimulatorMenu(createMenuListener());
        menu.init();
        
    // desktop engine
        DesktopEngineListener engineListener = createEngineListener();
        createDesktopEngine(aeDistPlugin,projectPlugin,engineListener);
        initDesktopEngine();
        
    // debug
        simulatorDebug = new SimulatorDebug(engine);
        createLuaBreakpointsListener();
        
    // globals
        simulatorGlobals = new SimulatorGlobals(engine);
        
    // traceback
        simulatorTraceback = new SimulatorTraceback(engine,simulatorGlobals);

    // component
        component = new SimulatorComponent(createComponentListener());
        initComponent();
        
    // UI
        initUI();
    }
    
    /** */
    private void createLuaBreakpointsListener() {
        LuaBreakpointsListener listener = new LuaBreakpointsListener() {
            /** */
            @Override
            public boolean breakpointAdded(LuaBreakpoint breakpoint) {
                addBreakpoint(breakpoint);
                return true;
            }
            
            /** */
            @Override
            public boolean breakpointRemoved(LuaBreakpoint breakpoint) {
                removeBreakpoint(breakpoint);
                return true;
            }
            
            /** */
            @Override
            public boolean breakpointStateChanged(LuaBreakpoint breakpoint) {
                if (breakpoint.isEnabled() == true) {
                    addBreakpoint(breakpoint);
                }
                else {
                    removeBreakpoint(breakpoint);
                }
                return true;
            }
            
            /** */
            @Override
            public boolean breakpointLineChanged(LuaBreakpoint breakpoint,
                int oldLine) {
            //
                removeBreakpoint(breakpoint.getSource(),oldLine);
                addBreakpoint(breakpoint);
                return true;
            }
        };
        LuaBreakpoints.get().addLuaBreakpointsListener(listener);
    }
    
    /** */
    private void initComponent() {
        component.init();
        component.setVolume(state.getVolume());        
        component.setResolution(state.getWidth(),state.getHeight());
    }
    
    /** */
    private SimulatorComponentListener createComponentListener() {
        SimulatorComponentListener listener = new SimulatorComponentListener() {
            /** */
            @Override
            public void startSimulator() {
                startEngine(false);
            }
            
            /** */
            @Override
            public void stopSimulator() {
                stopEngine();
            }
            
            /** */
            @Override
            public void pauseResumeSimulator() {
                pauseResumeEngine();
            }  
            
            /** */
            @Override
            public void debugSimulator() {
                startEngine(true);
            }
            
            /** */
            @Override
            public void volumeChanged(double volume) {
                setVolume(volume);
            }
            
            /** */
            @Override
            public void resolutionChanging(int width,int height) {
                setResolution(width,height);
            }
            
            /** */
            @Override
            public void resolutionChanged(int width,int height) {
                setResolution(width,height);
            }
        };
        return listener;
    }
    
    /** */
    private SimulatorMenuListener createMenuListener() {
        SimulatorMenuListener listener = new SimulatorMenuListener() {
            /** */
            @Override
            public void startSimulator() {
                startEngine(false);
            }
            
            /** */
            @Override
            public void stopSimulator() {
                stopEngine();
            }
            
            /** */
            @Override
            public void pauseResumeSimulator() {
                pauseResumeEngine();
            }        
            
            /** */
            @Override
            public void debugSimulator() {
                startEngine(true);
            }            
        };
        return listener;
    }
    
    /** */
    private DesktopEngineListener createEngineListener() {
        DesktopEngineListener listener = new DesktopEngineListener() {
            /** */
            @Override
            public void failedToRunEngine(Exception exception) {
                handleException(res.getStr("failed.to.run.engine",
                    exception.getMessage()),exception);
            }
            
            /** */
            @Override
            public void log(LogLevel level,String tag,String message) {
                SimulatorLog.get().log(level,tag,message);
            }
            
            /** */
            public void failedToStreamStandardError(IOException exception) {
                handleException(res.getStr("failed.to.stream.error",
                    exception.getMessage()),exception);
            }
            
            /** */
            @Override
            public void paused() {
                paused = true;
                component.paused();
                status.paused();
                menu.paused();                
            }
            
            /** */
            @Override
            public void resumed() {
                paused = false;
                component.resumed();
                status.resumed();
                menu.resumed();
            }
            
            /** */
            @Override
            public void stopping() {
                simulatorDebug.stopping();
            }
            
            /** */
            @Override
            public void processStopped() {
                Log.trace("Simulator process stopped");
                component.stopped();
                status.stopped();
                menu.stopped();
                simulatorDebug.stopped();
                LuaSnippet.get().setRunEnabled(true);
            }
            
            /** */
            @Override
            public void volumeSet(double volume) {
                component.setVolume(volume);
            }
                        
            /** */
            @Override
            public void suspended(String source,int line) {
                status.suspended();
                simulatorDebug.suspended(source,line);
            }
            
            /** */
            @Override
            public void failedToReadTraceback(IOException exception) {
                simulatorTraceback.failedToReadTraceback(exception);
            }
            
            /** */
            @Override
            public void receivedTraceback(LuaTraceback traceback) {
                simulatorTraceback.receivedTraceback(traceback);
            }    
            
            /** */
            @Override
            public void failedToReadTable(IOException exception,
                String requestId) {
            //
                simulatorTraceback.failedToReadTable(exception,requestId);
            }
            
            /** */
            @Override
            public void receivedTable(List<LuaValue> tableValues,
                String tablePointer,String requestId) {
            //
                simulatorTraceback.receivedTable(
                    tableValues,tablePointer,requestId);
                simulatorGlobals.receivedTable(
                    tableValues,tablePointer,requestId);
            }
            
            /** */
            @Override
            public void continuingExecution() {
                status.continuingExecution();
            }
            
            /** */
            @Override
            public void failedToReadGlobals(IOException exception) {
                simulatorGlobals.failedToReadGlobals(exception);
            }
            
            /** */
            @Override
            public void receivedGlobals(List<LuaValue> globals) {
                simulatorGlobals.receivedGlobals(globals);
            }
            
            /** */
            @Override
            public void frameRendered() {
            }
            
            /** */
            @Override
            public void doLuaStringFinished() {
                LuaSnippet.get().setRunEnabled(true);
            }
            
            /** */
            @Override
            public void failedToDoLuaString(String error) {
                LuaSnippet.get().setRunEnabled(true);
                handleError(res.getStr("failed.to.do.lua.string",error));
            }
        };
        return listener;
    }
    
    /** */
    private void createDesktopEngine(AEDistPlugin aeDistPlugin,
        ProjectPlugin projectPlugin,DesktopEngineListener engineListener) {
    //
        File aeDistDir = aeDistPlugin.getAEDistDir();
        try {
            aeDistDir = aeDistDir.getCanonicalFile();
        } catch (IOException exception) {
        }
    
        File execFile = DesktopEngineCfg.getExecFile(
            aeDistDir.getAbsolutePath());
        engineCfg = new DesktopEngineCfg(execFile,
            projectPlugin.getProjectDir());
        engineCfg.setErrorOutputStream(new ErrorOutputStream());
        engineCfg.setDebugLogFile(new File(storageDir,"debug.log"));
        engine = new DesktopEngine(engineCfg,engineListener);
    }
    
    /** */
    private void initDesktopEngine() {
        setVolume(state.getVolume());
    }
    
    /** */
    static void handleError(String message) {
        SimulatorLog.get().error(message);
    }
    
    /** */
    static void handleException(String message,Exception exception) {
        SimulatorLog.get().error(message);
        Log.error(message,exception);
    }
    
    /** */
    private void addBreakpoint(LuaBreakpoint breakpoint) {
        Log.trace(String.format("Simulator.addBreakpoint(%s:%d)",
            breakpoint.getSource(),breakpoint.getLine()));
        try {
            engine.addBreakpoint(breakpoint.getSource(),breakpoint.getLine());
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.add.breakpoint",
                exception.getMessage()),exception);
            return;
        }        
    }
    
    /** */
    private void removeBreakpoint(String source,int line) {
        Log.trace(String.format("Simulator.removeBreakpoint(%s:%d)",
            source,line));        
        try {
            engine.removeBreakpoint(source,line);
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.remove.breakpoint",
                exception.getMessage()),exception);
            return;
        }        
    }
    
    /** */
    private void removeBreakpoint(LuaBreakpoint breakpoint) {
        removeBreakpoint(breakpoint.getSource(),breakpoint.getLine());
    }
    
    /** */
    private boolean canStart() {        
    // Lua problems?
        if (Problems.get().hasErrors(LuaCompiler.PROBLEM_TYPE) == true) {
            if (CommonDialogs.yesNo(res.getStr("lua.errors.title"),
                res.getStr("lua.errors.msg")) == false) {
            //
                return false;
            }
        }
        
        boolean hasDirtyEditors = false;
    // dirty Lua text editors
        List<EditorMediator> mediators = TextEditor.get().getDirtyEditors();
        for (EditorMediator mediator:mediators) {
            if (ProjectLuaFiles.get().isLuaFile(mediator.getFile()) == true) {
                hasDirtyEditors = true;
                break;
            }
        }
        if (hasDirtyEditors == true) {
            if (CommonDialogs.yesNo(res.getStr("dirty.editors.title"),
                res.getStr("dirty.ediors.msg")) == false) {
            //
                return false;
            }            
        }
        
        return true;
    }
    
    /** */
    private void start(boolean debug) {
        if (canStart() == false) {
            return;
        }
        
        paused = false;
        component.running();
        status.running(debug);
        menu.running();
        
    // breakpoints
        if (debug == true) {
            try {
                engine.removeAllBreakpoints();
            } catch (IOException exception) {
                handleException(res.getStr("failed.to.remove.all.breakpoints",
                    exception.getMessage()),exception);
                return;                
            }
            
            for (LuaBreakpoint breakpoint:LuaBreakpoints.get().all()) {
                if (breakpoint.isEnabled() == true) {
                    addBreakpoint(breakpoint);
                }
            }
            simulatorDebug.starting();
        }
        
    // run
        engine.runEngineAsync(debug);
        showLog();          
    }
    
    /** */
    private void restart() {
        if (canStart() == false) {
            return;
        }        
        
        simulatorDebug.restarting();        
        try {
            engine.restartEngine();
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.restart.engine",
                exception.getMessage()),exception);
            return;
        }
        showLog();
    }
    
    /** */
    private void showLog() {
        if (state.getClearLog()) {
            SimulatorLog.get().clear();
        }
        if (state.getShowLog()) {
            SimulatorLog.get().show();
        }
    }
    
    /** */
    private void startEngine(boolean debug) {
        if (engine.isRunning() == false) {
            start(debug);
        }
        else {
            restart();
        }
    }
    
    /** */
    private void stopEngine() {
        if (engine.isRunning() == true) {
            try {
                engine.stopEngine();
            } catch (IOException exception) {
                handleException(res.getStr("failed.to.stop.engine",
                    exception.getMessage()),exception);
                return;
            }
        }
    }
    
    /** */
    private void pauseResumeEngine() {
        if (paused == true) {
        // resume
            try {
                engine.resumeEngine();
            } catch (IOException exception) {
                handleException(res.getStr("failed.to.resume.engine",
                    exception.getMessage()),exception);
                return;
            }            
        }
        else {
        // pause
            try {
                engine.pauseEngine();
            } catch (IOException exception) {
                handleException(res.getStr("failed.to.pause.engine",
                    exception.getMessage()),exception);
                return;
            }            
        }
    }
    
    /** */
    private void setVolume(double volume) {
        state.setVolume(volume);
        
        try {
            engine.setVolume(volume,false);
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.set.volume",
                exception.getMessage()),exception);
            return;
        }         
    }
    
    /** */
    private void initUI() {
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(SimulatorIcons.SIMULATOR);
        
    // view factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.DETAILS);
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                SimulatorViewActions.addActions(view);
            }
        });
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        DefaultViewProvider provider = new DefaultViewProvider(
            title,icon,factory,VIEW_ID);
        MainFrame.get().getViewManager().addViewProvider(provider);  
        
    // status
        UICommon.invokeAndWait(new Runnable() {
            public void run() {
                status = new SimulatorStatus();
                status.init();
                StatusBar.get().addStatusComponent(status);
            }
        });
    } 
    
    /**
     * Called when the plugin is stopping.
     */
    void stop() {
        stopEngine();
    }
    
    /** */
    private void setResolution(int width,int height) {
        state.setWidth(width);
        state.setHeight(height);
        engineCfg.setResolution(width,height);
        notifyResolutionChanged();
    }
    
    /** */
    private void notifyResolutionChanged() {
        synchronized (listeners) {
            for (SimulatorListener listener:listeners) {
                listener.simulatorResolutionChanged(getWidth(),getHeight());
            }
        }
    }
    
    /** */
    public void addSimulatorListener(SimulatorListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /**
     * Gets the simulator width.
     *
     * @return The simulator width.
     */
    public int getWidth() {
        return state.getWidth();
    }
    
    /**
     * Gets the simulator height.
     *
     * @return The simulator height.
     */
    public int getHeight() {
        return state.getHeight();
    }
    
    /** */
    public static Simulator get() {
        if (instance == null) {
            instance = new Simulator();
        }
        
        return instance;
    }
}