package com.andcreations.ae.desktop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.msgpack.map.MessagePackMap;

/**
 * @author Mikolaj Gucki
 */
class LuaDataReader {
    /** */
    private LuaValue readValue(MessagePackMap valueMap) throws IOException {
    // name
        String name = valueMap.getStr(CommProtocol.NAME_KEY);
        if (name == null) {
            throw new IOException("Missing name in Lua value");
        }
        
    // value
        String value = valueMap.getStr(CommProtocol.VALUE_KEY);
        if (value == null) {
            throw new IOException("Missing value in Lua value");
        }
        
    // scope
        LuaValue.Scope scope = null;
        if (valueMap.contains(CommProtocol.SCOPE_KEY) == true) {
            scope = CommProtocol.strToValueScope(
                valueMap.getStr(CommProtocol.SCOPE_KEY));
        }
        
    // type
        LuaValue.Type type = CommProtocol.strToValueType(
            valueMap.getStr(CommProtocol.TYPE_KEY));
        
    // value
        LuaValue luaValue = new LuaValue(name,value,scope,type);
        
    // pointer
        luaValue.setPointer(valueMap.getStr(CommProtocol.POINTER_KEY));
        
        return luaValue;        
    }
    
    /** */
    private List<LuaValue> readTracebackValues(MessagePackMap itemMap)
        throws IOException {
    //
        List<LuaValue> values = new ArrayList<>();
        
        int count = itemMap.getInt(CommProtocol.COUNT_KEY,-1);
        if (count <= 0) {
            return values;
        }
        
        for (int index = 0; index < count; index++) {
            MessagePackMap valueMap = itemMap.getMap("value." + index);
            if (valueMap == null) {
                throw new IOException("Missing Lua value map");
            }            
            
            values.add(readValue(valueMap));
        }
        
        return values;
    }
    
    /** */
    LuaTraceback readTraceback(MessagePackMap map)
        throws IOException {
    //
        int count = map.getInt(CommProtocol.COUNT_KEY,-1);
        if (count <= 0) {
            return null;
        }
        
        List<LuaTracebackItem> items = new ArrayList<>();
        for (int index = 0; index < count; index++) {            
            MessagePackMap itemMap = map.getMap("item." + index);
            if (itemMap == null) {
                throw new IOException("Missing traceback item map");
            }
            
        // source
            String source = itemMap.getStr(CommProtocol.SOURCE_KEY);
            if (source == null) {
                throw new IOException("Missing source in traceback item");
            }
            
        // line
            Integer line = itemMap.getInt(CommProtocol.LINE_KEY);
            if (line == null) {
                throw new IOException("Missing line in traceback item");
            }
            
        // what
            String whatStr = itemMap.getStr(CommProtocol.WHAT_KEY);
            if (whatStr == null) {
                throw new IOException("Missing what in traceback item");
            }
            LuaTracebackItem.What what = CommProtocol.strToWhat(whatStr);
            
        // function name
            String name = itemMap.getStr(CommProtocol.NAME_KEY);
            if (name == null) {
                throw new IOException("Missing name in traceback item");
            }
            
        // values
            List<LuaValue> values = readTracebackValues(itemMap);
                
        // item
            items.add(new LuaTracebackItem(
                index,source,line.intValue(),what,name,values));
        }
        
        return new LuaTraceback(items);
    }
    
    /** */
    String readTablePointer(MessagePackMap map) throws IOException {
        return map.getStr(CommProtocol.POINTER_KEY);
    }
    
    /** */
    List<LuaValue> readTable(MessagePackMap map) throws IOException {
    // value count
        int count = map.getInt(CommProtocol.COUNT_KEY,-1);
        if (count < 0) {
            return null;
        }
        
        List<LuaValue> tableValues = new ArrayList<>();
    // for each value
        for (int index = 0; index < count; index++) {
            MessagePackMap valueMap = map.getMap("value." + index);
            if (valueMap == null) {
                throw new IOException("Missing table value map");
            }
         
            LuaValue tableValue = readValue(valueMap);
            tableValues.add(tableValue);
        }
        
        return tableValues;
    }
}