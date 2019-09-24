package com.andcreations.ae.desktop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.io.Base16InputStream;
import com.andcreations.ae.io.Base16OutputStream;
import com.andcreations.msgpack.map.MessagePackMap;
import com.andcreations.msgpack.map.MessagePackMapPacker;

/**
 * Responsible for running and controlling the desktop engine process.
 *
 * @author Mikolaj Gucki
 */
public class DesktopEngine {
    /** The configuration. */
    private DesktopEngineCfg cfg;
    
    /** The listener. */
    private DesktopEngineListener listener;
    
    /** The desktop engine process. */
    private Process process;
    
    /** Indicates if debug is enabled. */
    private boolean debugEnabled;
    
    /** The output stream. */
    private Base16OutputStream output;
    
    /** The message dispatcher.*/
    private CommMsgDispatcher dispatcher;
    
    /** Indicates if the engine is running. */
    private boolean running;
    
    /** Indicates if the execution is suspended. */
    private boolean suspended;
    
    /** The volume recently set. */
    private double volume;
    
    /** The breakpoints. */
    private List<DesktopEngineBreakpoint> breakpoints = new ArrayList<>();
    
    /** */
    public DesktopEngine(DesktopEngineCfg cfg,DesktopEngineListener listener) {
        this.cfg = cfg;
        this.listener = listener;
    }
     
    /** */
    private File getAEDistDir() {
        File aeDistDir = cfg.getExecFile().getParentFile().getParentFile();        
        try {
            aeDistDir = aeDistDir.getCanonicalFile();
        } catch (IOException exception) {            
        }
        
        return aeDistDir;
    }
    
    /** */
    private boolean isOSX() {
        String name = System.getProperty("os.name").toLowerCase();
        return name.contains("mac");
    }    
    
    /** */
    private ProcessBuilder buildProcess() {
        List<String> args = new ArrayList<>();
        args.add(cfg.getExecFile().getAbsolutePath());
        args.add("--comm");
        args.add("--no-stats");
        if (debugEnabled == true) {
            args.add("--comm-debug");
        }
        if (cfg.getDebugLogFile() != null) {
            args.add("--debug-log=" + cfg.getDebugLogFile().getAbsolutePath());
        }
        args.add("audio=yes");
        
    // volume
        int volume100 = (int)(volume * 100);
        args.add(String.format("volume=%d",volume100));
        
    // resolution
        if (cfg.hasResolution()) {
            args.add(String.format("win.size=%dx%d",cfg.getWidth(),
                cfg.getHeight()));
        }            
        
        ProcessBuilder builder = new ProcessBuilder(args);
        builder.directory(cfg.getProjectDir());

    // framework path (OS X)
        if (isOSX()) {
            String dyldFrameworkPath = System.getenv("DYLD_FRAMEWORK_PATH");
            if (dyldFrameworkPath != null) {
                dyldFrameworkPath += ":";
            }
            else {
                dyldFrameworkPath = "";
            }
            dyldFrameworkPath +=
                new File(getAEDistDir(),"libs/osx").getAbsolutePath();
            System.out.println("dyldFrameworkPath=" + dyldFrameworkPath);
            builder.environment().put("DYLD_FRAMEWORK_PATH",dyldFrameworkPath);   
            for (String k:builder.environment().keySet()) {
                System.out.println(k + "=" + builder.environment().get(k));
            }
        }        
        
        return builder;
    }
    
    /** */
    private void startWaitForThread() {
        Thread thread = new Thread(new Runnable() {
            /** */  
            @Override
            public void run() {
                try {
                    process.waitFor();
                } catch (InterruptedException exception) {
                }
                process = null;
                processStopped();
            }
        });  
        thread.setName("DesktopEngineWaitFor");
        thread.start();
    }
    
    /** */
    private void processStopped() {
        running = false;
        listener.processStopped();
    }
    
    /** */
    private void startErrorStreamThread(final InputStream errorStream) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {                        
                    try {
                        int value = errorStream.read();
                        if (value == -1) {
                            return;
                        }
                        if (cfg.getErrorOutputStream() != null) {
                            cfg.getErrorOutputStream().write(value);
                            cfg.getErrorOutputStream().flush();
                        }
                    } catch (IOException exception) {
                        listener.failedToStreamStandardError(exception);
                        return;
                    }
                }
            }
        });
        thread.setName("DesktopEngineErrorStream");
        thread.start();
    }
    
    /**
     * Runs the engine synchronously.
     *
     * @param debug Indicates if to the enable debug.
     * @throws Exception if the engine cannot run.
     */
    public void runEngine(boolean debug) throws Exception {
        this.debugEnabled = debug;
        ProcessBuilder builder = buildProcess();
        
    // start
        try {
            process = builder.start();
        } catch (IOException exception) {
            failedToRunEngine(exception);
            processStopped();
            return;
        }
        
    // output
        output = new Base16OutputStream(process.getOutputStream());
        
    // wait-for thread
        startWaitForThread();
        
    // error stream
        startErrorStreamThread(process.getErrorStream());
        
    // message dispatcher
        InputStream input = new Base16InputStream(process.getInputStream());
        dispatcher = new CommMsgDispatcher(this,input,listener);
        try {
            dispatcher.runDispatcher();
        } catch (IOException exception) {
            failedToRunEngine(exception);
            return;
        }
    }
    
    /** */
    private void failedToRunEngine(Exception exception) {
        listener.failedToRunEngine(exception);                    
    }
    
    /**
     * Runs the engine asynchronously.
     *
     * @param debug Indicates if to enable debug.
     */
    public void runEngineAsync(final boolean debug) {
        Runnable runnable = new Runnable() {
            /** */
            @Override
            public void run() {
                try {
                    runEngine(debug);
                } catch (Exception exception) {
                    failedToRunEngine(exception);
                }
            }
        };
        Thread thread = new Thread(runnable,"DesktopEngine");
        thread.start();
    }
    
    /**
     * Runs the engine asynchronously with debug disabled.
     */
    public void runEngineAsync() {
        runEngineAsync(false);
    }
    
    /** */
    void send(MessagePackMap map) throws IOException {
        if (process == null) {
            return;
        }
        
        MessagePackMapPacker.pack(output,map);
        output.flush();
    }
    
    /** */
    void setRunning() {
        running = true;
    }
    
    /** */
    public boolean isRunning() {
        return running;
    }
    
    /** */
    public boolean isDebug() {
        return debugEnabled;
    }
    
    /** */
    public void pauseEngine() throws IOException {
        MessagePackMap map = CommProtocol.newMap(CommProtocol.MsgId.PAUSE);
        send(map);
    }
    
    /** */
    public void resumeEngine() throws IOException {
        MessagePackMap map = CommProtocol.newMap(CommProtocol.MsgId.RESUME);
        send(map);
    }        
    
    /** */
    public void restartEngine() throws IOException {
        if (suspended == true) {
            continueExecution();
        }
        MessagePackMap map = CommProtocol.newMap(CommProtocol.MsgId.RESTART);
        send(map);
    }        
    
    /** */
    public void stopEngine() throws IOException {
        if (suspended == true) {
        // TODO Detach debugger?
            continueExecution();
        }
        
        dispatcher.postStop();
        MessagePackMap map = CommProtocol.newMap(CommProtocol.MsgId.STOP);
        send(map);
    }
    
    /** */
    public void setVolume(double volume,boolean notify) throws IOException {
        MessagePackMap map = CommProtocol.newMap(CommProtocol.MsgId.SET_VOLUME);
        map.put(CommProtocol.VOLUME_KEY,(int)(volume * 100));
        map.put(CommProtocol.NOTIFY_KEY,CommProtocol.getBoolean(notify));
        send(map);        
        this.volume = volume;
    }
    
    /** */
    private void checkDebugEnabled() {
        if (debugEnabled == false) {
            throw new IllegalStateException("Debug disabled");
        }        
    }
    
    /** */
    private DesktopEngineBreakpoint findBreakpoint(String source,int line) {
        for (DesktopEngineBreakpoint breakpoint:breakpoints) {
            if (breakpoint.getSource().equals(source) == true &&
                breakpoint.getLine() == line) {
            //
                return breakpoint;
            }
        }
        
        return null;
    }
    
    /** */
    private void doAddBreakpoint(String source,int line) throws IOException {
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(
            CommProtocol.MsgId.ADD_BREAKPOINT);
        map.put(CommProtocol.SOURCE_KEY,source);
        map.put(CommProtocol.LINE_KEY,line);
        send(map); 
    }
    
    /** */
    private String fixBreakpointSource(String source) {
        source = source.replace('\\','/');
        if (source.startsWith("/") == true) {
            source = source.substring(1,source.length());
        }
        if (source.endsWith(".lua") == false) {
            source = source.replace('.','/') + ".lua";
        } 

        return source;        
    }
    
    /** */
    public void addBreakpoint(String source,int line) throws IOException {
        source = fixBreakpointSource(source);
    
    // add to the list
        breakpoints.add(new DesktopEngineBreakpoint(source,line));
        
    // add to the engine
        if (isRunning() == true && isDebug() == true) {
            doAddBreakpoint(source,line);
        }
    }
    
    /** */
    private void doRemoveBreakpoint(String source,int line) throws IOException {
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(
            CommProtocol.MsgId.REMOVE_BREAKPOINT);
        map.put(CommProtocol.SOURCE_KEY,source);
        map.put(CommProtocol.LINE_KEY,line);
        send(map); 
    }    
    
    /** */
    public boolean removeBreakpoint(String source,int line) throws IOException {
        source = fixBreakpointSource(source);        
        
    // remove from the list
        DesktopEngineBreakpoint breakpoint = findBreakpoint(source,line);
        if (breakpoint == null) {
            return false;
        }
        breakpoints.remove(breakpoint);
        
    // remove from the engine
        if (isRunning() == true && isDebug() == true) {
            doRemoveBreakpoint(source,line);
        }
        
        return true;
    }
    
    /** */
    public void removeAllBreakpoints() throws IOException {
        while (breakpoints.isEmpty() == false) {
            DesktopEngineBreakpoint breakpoint = breakpoints.remove(0);           
            removeBreakpoint(breakpoint.getSource(),breakpoint.getLine());
        }
    }
    
    /** */
    void flushBreakpoints() throws IOException {
        if (debugEnabled == true) {
        // clear
            MessagePackMap map = CommProtocol.newMap(
                CommProtocol.MsgId.CLEAR_BREAKPOINTS);
            send(map);            
            
        // add
            for (DesktopEngineBreakpoint breakpoint:breakpoints) {
                doAddBreakpoint(breakpoint.getSource(),breakpoint.getLine());
            }
        }
    }
    
    /** */
    void hooked(String source,int line) {
        // Not used as it's very slow.
    }
    
    /** */
    void suspended(String source,int line) {
        suspended = true;
        if (listener != null) {
            listener.suspended(source,line);
        }
    }
    
    /** */
    public void continueExecution() throws IOException {
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(
            CommProtocol.MsgId.CONTINUE_EXECUTION);
        send(map);
        
        suspended = false;
    }
    
    /** */
    public void stepInto() throws IOException {
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(CommProtocol.MsgId.STEP_INTO);
        send(map);       
        
        suspended = false;
    }
    
    /** */
    public void stepOver() throws IOException {
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(CommProtocol.MsgId.STEP_OVER);
        send(map);       
        
        suspended = false;
    }
    
    /** */
    public void stepReturn() throws IOException {
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(
            CommProtocol.MsgId.STEP_RETURN);
        send(map);            
        
        suspended = false;
    }

    /** */
    public void getTraceback() throws IOException {
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(
            CommProtocol.MsgId.GET_TRACEBACK);
        send(map); 
    }
    
    /** */
    public void findTableInTraceback(String tablePointer,String requestId)
        throws IOException {
    //
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(
            CommProtocol.MsgId.FIND_TABLE_IN_TRACEBACK);
        map.put(CommProtocol.REQUEST_ID_KEY,requestId);
        map.put(CommProtocol.POINTER_KEY,tablePointer);
        send(map);        
    }
    
    /** */
    public void getGlobals() throws IOException {
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(
            CommProtocol.MsgId.GET_GLOBALS);
        send(map);                
    }
    
    /** */
    public void findTableInGlobals(String tablePointer,String requestId)
        throws IOException {
    //
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(
            CommProtocol.MsgId.FIND_TABLE_IN_GLOBALS);
        map.put(CommProtocol.REQUEST_ID_KEY,requestId);
        map.put(CommProtocol.POINTER_KEY,tablePointer);
        send(map);
    }
    
    /** */
    public void doLuaString(String str) throws IOException{
        checkDebugEnabled();
        
    // send message
        MessagePackMap map = CommProtocol.newMap(
            CommProtocol.MsgId.LUA_DO_STRING);
        map.put(CommProtocol.SOURCE_KEY,str);        
        send(map);
    }
}
