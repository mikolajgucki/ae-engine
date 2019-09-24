package com.andcreations.ae.desktop;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.andcreations.msgpack.map.MessagePackMap;
import com.andcreations.msgpack.map.MessagePackMapUnpacker;

/**
 * The message dispatcher.
 *
 * @author Mikolaj Gucki
 */
public class CommMsgDispatcher {
    /** The engine. */
    private DesktopEngine engine;
    
    /** The input stream. */
    private InputStream input;
    
    /** The listener. */
    private DesktopEngineListener listener;

    /** */
    private boolean stopping;
    
    /** The data reader. */
    private LuaDataReader luaDataReader = new LuaDataReader();
    
    /** */
    CommMsgDispatcher(DesktopEngine engine,InputStream input,
        DesktopEngineListener listener) {
    //
        this.engine = engine;
        this.input = input;
        this.listener = listener;
    }

    /** */
    private String getStr(MessagePackMap map,String key) throws IOException {
        String value = map.getStr(key);
        if (value == null) {
            throw new IOException(String.format(
                "Missing string value in received message pack (%s)",key));
        }
        
        return value;        
    }
    
    /** */
    private int getInt(MessagePackMap map,String key) throws IOException {
        Integer value = map.getInt(key);
        if (value == null) {
            throw new IOException(String.format(
                "Missing integer value in received message pack (%s)",key));
        }
        
        return value.intValue();
    }
    
    /** */
    private void dispatch(MessagePackMap map) throws IOException {
        Integer msgIdInt = map.getInt(CommProtocol.MSG_ID_KEY);
        if (msgIdInt == null) {
            throw new IOException(
                "Received message without identifier: " + map);
        }
        
        int msgId = msgIdInt.intValue();
    // log
        if (msgId == CommProtocol.MsgId.LOG) {
            LogLevel logLevel = LogLevel.getById(getInt(map,"level"));
            listener.log(logLevel,getStr(map,"tag"),getStr(map,"msg"));
        }
        
    // paused
        if (msgId == CommProtocol.MsgId.PAUSED) {
            listener.paused();
        }
        
    // resumed
        if (msgId == CommProtocol.MsgId.RESUMED) {
            listener.resumed();
        }
        
    // stopping
        if (msgId == CommProtocol.MsgId.STOPPING) {
            stopping = true;
            MessagePackMap ack = CommProtocol.newMap(CommProtocol.MsgId.ACK);
            engine.send(ack);
            listener.stopping();
        }
        
    // volume set
        if (msgId == CommProtocol.MsgId.VOLUME_SET) {
            double volume = map.getInt(CommProtocol.VOLUME_KEY) / (double)100;
            listener.volumeSet(volume);
        }
        
    // paused before entering the loop
        if (msgId == CommProtocol.MsgId.PAUSED_BEFORE_ENTERING_LOOP) {
            enterLoop();
        }
        
    // suspended
        if (msgId == CommProtocol.MsgId.SUSPENDED) {
            String source = map.getStr(CommProtocol.SOURCE_KEY);
            int line = map.getInt(CommProtocol.LINE_KEY);
            engine.suspended(source,line);
        }
        
    // traceback
        if (msgId == CommProtocol.MsgId.TRACEBACK) {
            LuaTraceback traceback = null;
            try {
                traceback = luaDataReader.readTraceback(map);
            } catch (IOException exception) {
                listener.failedToReadTraceback(exception);
            }
            if (traceback != null) {
                listener.receivedTraceback(traceback);
            }
        }
        
    // continuing execution
        if (msgId == CommProtocol.MsgId.CONTINUING_EXECUTION) {
            listener.continuingExecution();
        }
        
    // table
        if (msgId == CommProtocol.MsgId.TABLE) {
            String requestId = map.getStr(CommProtocol.REQUEST_ID_KEY);
            String tablePointer = luaDataReader.readTablePointer(map);
            List<LuaValue> tableValues = null;
            try {
                tableValues = luaDataReader.readTable(map);
            } catch (IOException exception) {
                listener.failedToReadTable(exception,requestId);
                return;
            }
            listener.receivedTable(tableValues,tablePointer,requestId);
        }
        
    // globals
        if (msgId == CommProtocol.MsgId.GLOBALS) {
            List<LuaValue> globals = null;
            try {
                globals = luaDataReader.readTable(map);
            } catch (IOException exception) {
                listener.failedToReadGlobals(exception);
                return;                
            }
            listener.receivedGlobals(globals);
        }
        
    // hooked
        if (msgId == CommProtocol.MsgId.HOOKED) {
            String source = map.getStr(CommProtocol.SOURCE_KEY);
            int line = map.getInt(CommProtocol.LINE_KEY);
            engine.hooked(source,line);
        }
        
    // Lua code execution finished
        if (msgId == CommProtocol.MsgId.MSG_ID_LUA_DO_STRING_FINISHED) {
            listener.doLuaStringFinished();
        }
        
    // Lua code execution failed
        if (msgId == CommProtocol.MsgId.LUA_DO_STRING_FAILED) {
            String errorMsg = map.getStr(CommProtocol.ERROR_MSG_KEY);
            listener.failedToDoLuaString(errorMsg);
        }
    }
    
    /** */
    private void enterLoop() throws IOException {
    // send the breakpoints
        engine.flushBreakpoints();
        
    // send message
        MessagePackMap enterLoop = CommProtocol.newMap(
            CommProtocol.MsgId.ENTER_LOOP);
        engine.send(enterLoop);
        
    // flag as running
        engine.setRunning();
    }
    
    /** */
    void postStop() {
        stopping = true;
    }
    
    /**
     * Runs the dispatcher.
     * @throws IOException on I/O error.
     */
    public void runDispatcher() throws IOException {
        /*
        InputStream echo = new InputStream() {
            @Override
            public int read() throws IOException {
                int value = input.read();
                System.out.print((char)value);
                return value;
            }
        };
        */
        
        while (true) {
            try {
                MessagePackMap map = MessagePackMapUnpacker.unpack(input);
                dispatch(map);
            } catch (IOException exception) {
                if (stopping == true) {
                    return;
                }
                throw exception;
            }
        }
    }
}