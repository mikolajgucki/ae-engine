package com.andcreations.ae.studio.plugins.simulator;

import java.io.IOException;
import java.util.List;

import com.andcreations.ae.desktop.DesktopEngine;
import com.andcreations.ae.desktop.LuaValue;
import com.andcreations.ae.studio.plugins.lua.debug.globals.Globals;
import com.andcreations.ae.studio.plugins.lua.debug.globals.GlobalsItem;
import com.andcreations.ae.studio.plugins.lua.debug.globals.GlobalsItemAdapter;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class SimulatorGlobals {
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
    private BundleResources res = new BundleResources(SimulatorGlobals.class);
    
    /** */
    private DesktopEngine engine;    
    
    /** */
    private int idSeq;
    
    /** The globals received most recent. */
    private List<LuaValue> globals;
    
    /** */
    SimulatorGlobals(DesktopEngine engine) {
        this.engine = engine;
    }
    
    /** */
    private void handleException(String message,Exception exception) {
        Simulator.handleException(message,exception);
    }
    
    /** */
    synchronized LuaValue findByPointer(String pointer) {
        if (pointer == null || globals == null) {
            return null;
        }
        
        for (LuaValue global:globals) {
            if (pointer.equals(global.getPointer()) == true) {
                return global;
            }
        }
        
        return null;
    }
    
    /** */
    synchronized void failedToReadGlobals(IOException exception) {
        this.globals = null;
        handleException(res.getStr("failed.to.read.globals",
            exception.getMessage()),exception);
    }
    
    /** */
    private void luaTableExpanded(LuaValue luaValue,GlobalsItem item) {
        LuaValueWrapper wrapper = (LuaValueWrapper)item.getObject();
        if (wrapper.expanded == true) {
            return;
        }
        wrapper.expanded = true;

        try {
            engine.findTableInGlobals(luaValue.getPointer(),wrapper.id);
        } catch (IOException exception) {
            handleException(res.getStr("failed.to.get.table",
                exception.getMessage()),exception);
            return;
        }        
    }

    /** */
    private void addTableItemListener(final LuaValue luaValue,
        GlobalsItem valueItem) {
    //
        valueItem.addGlobalsItemListener(new GlobalsItemAdapter() {
            /** */
            @Override
            public void globalsItemExpanded(GlobalsItem item) {
                luaTableExpanded(luaValue,item);
            }
        });        
    }    
    
    /** */
    private GlobalsItem createLuaValueItem(LuaValue luaValue,
        boolean withScope) {
    //
        String text = SimulatorLuaValue.get().toText(luaValue,withScope);
        String html = SimulatorLuaValue.get().toHTML(luaValue,withScope);
        
    // item
        GlobalsItem valueItem = new GlobalsItem(null,text,html,
            new LuaValueWrapper(luaValue));
        if (luaValue.getType() == LuaValue.Type.TABLE) {
            valueItem.setCanHaveChildren(true);
            addTableItemListener(luaValue,valueItem);
        }
        
        return valueItem;        
    }
    
    /** */
    synchronized void receivedGlobals(List<LuaValue> globals) {
        this.globals = globals;
        
    // create items
        GlobalsItem rootItem = new GlobalsItem(null,res.getStr("globals"));        
        for (LuaValue global:globals) {
            GlobalsItem valueItem = createLuaValueItem(global,true);
            rootItem.add(valueItem);
        }
        
        Globals.get().setGlobals(rootItem);
    }
    
    /** */
    private void addLuaValues(GlobalsItem parent,List<LuaValue> luaValues) {
        for (LuaValue luaValue:luaValues) {
            GlobalsItem item = createLuaValueItem(luaValue,false);
            Globals.get().addChildItem(parent,item);
        }
    }    
    
    /**
     * Called when a table has been received. Appends child values
     */
    void receivedTable(List<LuaValue> tableValues,String tablePointer,
        String requestId) {
    //
        List<GlobalsItem> items = Globals.get().getAllItems();
        for (GlobalsItem item:items) {
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