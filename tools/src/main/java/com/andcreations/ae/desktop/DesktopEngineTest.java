package com.andcreations.ae.desktop;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class DesktopEngineTest implements DesktopEngineListener {
    static DesktopEngine engine;
    
    /** */
    private String trim(String str,int length) {
        if (str.length() > length) {
            return str.substring(0,length);
        }
        
        return String.format("%" + length + "s",str);
    }
    
    /** */
    @Override
    public void failedToRunEngine(Exception exception) {
        System.err.println("failedToRunEngine");
        exception.printStackTrace();
    }
    
    /** */
    @Override
    public void log(LogLevel level,String tag,String message) {
        System.out.println(String.format("%s [%s] %s",trim(level.toString(),4),trim(tag,12),message));
    }
    
    /** */
    @Override
    public void failedToStreamStandardError(IOException exception) {
        exception.printStackTrace();
    }
    
    /** */
    @Override
    public void stopping() {
        //System.err.println(">>> Stopping");
    }
    /** */
    @Override
    public void paused() {
        //System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> paused");
    }
    /** */
    @Override
    public void resumed() {
        //System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> resumed");
    }
    
    /** */
    @Override
    public void processStopped() {
        //System.err.println(">>> Process stopped!");
    }
    
    /** */
    @Override
    public void volumeSet(double volume) {
        //System.err.println(">>> volume " + volume);
    }
    
    /** */
    @Override
    public void failedToReadTraceback(IOException e) {
        e.printStackTrace();
    }
    
    /** */
    @Override
    public void failedToReadTable(IOException e,String requestId) {
        e.printStackTrace();
    }
    
    /** */
    @Override
    public void frameRendered() {
    }
    
    /** */
    @Override
    public void doLuaStringFinished() {
    }
    
    /** */
    @Override
    public void receivedTraceback(LuaTraceback traceback) {
        //System.out.println("receivedTraceback ---####################");
        boolean got = false;
        for (LuaTracebackItem item:traceback.getItems()) {
            //System.out.println(item);
            for (LuaValue v:item.getValues()) {
                if (v.getType() == LuaValue.Type.TABLE && got == false &&
                    v.getName().equals("x")) {
                    //got = true;
                    /*System.out.println("get table " + v.getName() + "@" + v.getPointer());
                    try {
                        engine.getTable(v.getPointer(),"reqid");
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }*/
                }
            }
        }
        try {
            engine.continueExecution();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
     
    /** */
    @Override
    public void receivedTable(List<LuaValue> tableValues,String tablePointer,
        String requestId) {
    //
        System.out.println("---- recv'd table " + tablePointer);
        for (LuaValue value:tableValues) {
            System.out.println("  " + value);
        }
    }        
    
    /** */
    @Override
    public void suspended(String source,int line) {
        System.out.println("----------------------------------- " + source + ":" + line);
                
        try {
            engine.getTraceback();
            engine.getGlobals();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
    
    /** */
    @Override
    public void continuingExecution() {
        System.out.println("continuingExecution");
    }
    
    /** */
    @Override
    public void receivedGlobals(List<LuaValue> globals) {
        System.out.println("---- recv'd globals");
        for (LuaValue value:globals) {
            System.out.println("  " + value);
        }        
    }
    
    /** */
    @Override
    public void failedToReadGlobals(IOException exception) {
        System.err.println("Failed to recv globals: " + exception.getMessage());
    }

    /** */
    @Override
    public void failedToDoLuaString(String msg) {
        System.err.println("failedToDoLuaString: " + msg);
    }
    
    /** */
    public static void main(String[] args) throws Exception {
        DesktopEngineTest test = new DesktopEngineTest();
        DesktopEngineCfg cfg = new DesktopEngineCfg(
            //DesktopEngineCfg.getExecFile("e:/andcreations/ae/dist/build/dist"),
            new File("f:/1/ae/engine/desktop/ae.exe"),
            new File("f:/1/bubble.unblock/project"));
        cfg.setErrorOutputStream(System.err);
        
        engine = new DesktopEngine(cfg,test);
        //engine.addBreakpoint("root",85);
        //engine.addBreakpoint("bu.game.LevelLoader",323);
        engine.setVolume(0.1,false);
        
        /*
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(8 * 1000);
                    System.out.println("setting volume");
                    engine.setVolume(0.2,true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        */
        engine.runEngineAsync(true);
    }    
}