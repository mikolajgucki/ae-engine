package com.andcreations.ae.studio.plugins.simulator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.andcreations.ae.desktop.DesktopEngine;
import com.andcreations.ae.desktop.LuaTraceback;
import com.andcreations.ae.desktop.LuaTracebackItem;
import com.andcreations.ae.desktop.LuaValue;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.debug.traceback.Traceback;
import com.andcreations.ae.studio.plugins.lua.debug.traceback.TracebackItem;
import com.andcreations.ae.studio.plugins.lua.debug.traceback.TracebackItemAdapter;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class SimulatorTraceback {
    /** */
    private class LuaValueWrapper {
        /** */
        private String id;
        
        /** */
        @SuppressWarnings("unused")
        private LuaValue luaValue;
        
        /** */
        private boolean expanded;
        
        /** */
        private LuaValueWrapper(LuaValue luaValue) {
            this.luaValue = luaValue;
            this.id = Integer.toString(idSeq);
            idSeq++;
        }
    }
    
    /** */
    private BundleResources res = new BundleResources(SimulatorTraceback.class);  
    
    /** */
    private DesktopEngine engine;
    
    /** */
    private SimulatorGlobals globals;
    
    /** */
    private int idSeq;
    
    /** */ 
    private String dark;     
    
    /** */
    SimulatorTraceback(DesktopEngine engine,SimulatorGlobals globals) {
        this.engine = engine;
        this.globals = globals;
        create();
    }
    
    /** */
    private void create() {
        dark = UIColors.toHex(UIColors.dark());
    }    
    
    /** */
    private void handleException(String message,Exception exception) {
        Simulator.handleException(message,exception);
    }
    
    /** */
    void failedToReadTraceback(IOException exception) {
        handleException(res.getStr("failed.to.read.traceback",
            exception.getMessage()),exception);
    }
    
    /** */
    private String getShortSource(String source) {
        int index = source.lastIndexOf('/');
        if (index < 0) {
            return source;
        }
        return source.substring(index + 1,source.length());
    }
    
    /** */
    private void itemSelected(LuaTracebackItem luaItem) {
        File file = ProjectLuaFiles.get().getFileByPath(
            "/" + luaItem.getSource());
        if (file != null) {
            int line = luaItem.getLine();
            LuaFile.edit(file,line,line + 8);
        }
    }
    
    /** */
    private void addTracebackItemListener(final LuaTracebackItem luaItem,
        TracebackItem item) {
    //
        if (luaItem.getWhat() != LuaTracebackItem.What.LUA_FUNCTION &&
            luaItem.getWhat() != LuaTracebackItem.What.MAIN_CHUNK &&
            luaItem.getWhat() != LuaTracebackItem.What.TAIL_FUNCTION) {
        //
            return;
        }
        
        item.addTracebackItemListener(new TracebackItemAdapter() {
            /** */
            @Override
            public void tracebackItemSelected(TracebackItem item) {
                itemSelected(luaItem);
            }
        });
    }
    
    /** */
    private void luaTableExpanded(LuaValue luaValue,TracebackItem item) {
        LuaValueWrapper wrapper = (LuaValueWrapper)item.getObject();
        if (wrapper.expanded == true) {
            return;
        }
        wrapper.expanded = true;
        
        try {
            engine.findTableInTraceback(luaValue.getPointer(),wrapper.id);
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.get.table",
                exception.getMessage()),exception);
            return;
        }
    }    
    
    /** */
    private void addTableItemListener(final LuaValue luaValue,
        TracebackItem valueItem) {
    //
        valueItem.addTracebackItemListener(new TracebackItemAdapter() {
            /** */
            @Override
            public void tracebackItemExpanded(TracebackItem item) {
                luaTableExpanded(luaValue,item);
            }
        });        
    }
    
    /** */
    private TracebackItem createLuaValueItem(LuaValue luaValue,
        boolean withScope) {
    //    
        String text = SimulatorLuaValue.get().toText(
            luaValue,withScope,globals);
        String html = SimulatorLuaValue.get().toHTML(
            luaValue,withScope,globals);
        
    // item
        TracebackItem valueItem = new TracebackItem(null,text,html,
            new LuaValueWrapper(luaValue));
        if (luaValue.getType() == LuaValue.Type.TABLE) {
            valueItem.setCanHaveChildren(true);
            addTableItemListener(luaValue,valueItem);
        }
        
        return valueItem;
    }
    
    /** */
    private void addLuaValues(LuaTracebackItem luaItem,TracebackItem item) {
        for (LuaValue luaValue:luaItem.getValues()) {
            TracebackItem valueItem = createLuaValueItem(luaValue,true);            
            item.add(valueItem);
        }
    }
    
    /** */
    void receivedTraceback(LuaTraceback traceback) {
        TracebackItem rootItem = new TracebackItem(
            null,res.getStr("traceback"));
        for (LuaTracebackItem luaItem:traceback.getItems()) {
            String source = getShortSource(luaItem.getSource());
            
            String resKeySuffix = "";
            if (luaItem.getWhat() == LuaTracebackItem.What.LUA_FUNCTION &&
                luaItem.getName().length() == 0) {
            //
                resKeySuffix += ".no.func";
            }

        // text
            String text = res.getStr(String.format("traceback.text.%s%s",
                luaItem.getWhat().name(),resKeySuffix),luaItem.getName(),
                source,Integer.toString(luaItem.getLine()));
            
        // html
            String html = res.getStr(String.format("traceback.html.%s%s",
                luaItem.getWhat().name(),resKeySuffix),luaItem.getName(),
                source,Integer.toString(luaItem.getLine()),dark);
        // item
            TracebackItem item = new TracebackItem(null,text,html);
            addTracebackItemListener(luaItem,item);
            rootItem.add(item);
            
        // values
            addLuaValues(luaItem,item);
        }
                
        Traceback.get().setTraceback(rootItem);
    }    
    
    /** */
    void failedToReadTable(IOException exception,String requestId) {
        handleException(res.getStr("failed.to.read.table",
            exception.getMessage()),exception);
    }
    
    /** */
    private void addLuaValues(TracebackItem parent,List<LuaValue> luaValues) {
        for (LuaValue luaValue:luaValues) {
            TracebackItem item = createLuaValueItem(luaValue,false);
            Traceback.get().addChildItem(parent,item);
        }
    }
    
    /** */
    void receivedTable(List<LuaValue> tableValues,String tablePointer,
        String requestId) {
    //
        List<TracebackItem> items = Traceback.get().getAllItems();
        for (TracebackItem item:items) {
            if (item.getObject() != null) {
                LuaValueWrapper wrapper = (LuaValueWrapper)item.getObject();
                if (requestId.equals(wrapper.id) == true) {
                    addLuaValues(item,tableValues);
                    return;
                }
            }
        }
    }
}