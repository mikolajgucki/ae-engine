package com.andcreations.msgpack.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * A map that can packed to and unpacked from a message pack.
 *
 * @author Mikolaj Gucki 
 */
public class MessagePackMap {
    /** The map itself. */
    private Map<Object,Object> map = new HashMap<Object,Object>();
    
    /**
     * Puts a boolean value in the map.
     *
     * @param key The key.
     * @param value The value.
     */
    public void put(String key,boolean value) {
        map.put(key,Boolean.valueOf(value));
    }
    
    /**
     * Puts a boolean value in the map.
     *
     * @param key The key.
     * @param value The value.
     */
    public void put(int key,boolean value) {
        map.put(Integer.valueOf(key),Boolean.valueOf(value));
    }
    
    /**
     * Puts an integer value in the map.
     *
     * @param key The key.
     * @param value The value.
     */
    public void put(String key,int value) {
        map.put(key,Integer.valueOf(value));
    }        
    
    /**
     * Puts an integer value in the map.
     *
     * @param key The key.
     * @param value The value.
     */
    public void put(int key,int value) {
        map.put(Integer.valueOf(key),Integer.valueOf(value));
    }        
        
    /**
     * Puts a string value in the map.
     *
     * @param key The key.
     * @param value The value.
     */
    public void put(String key,String value) {
        map.put(key,value);
    }
    
    /**
     * Puts a string value in the map.
     *
     * @param key The key.
     * @param value The value.
     */
    public void put(int key,String value) {
        map.put(Integer.valueOf(key),value);
    }
    
    /**
     * Puts a map in the map.
     *
     * @param key The key.
     * @param value The value.
     */
    public void put(String key,MessagePackMap value) {
        map.put(key,value);
    }
    
    /**
     * Puts a map in the map.
     *
     * @param key The key.
     * @param value The value.
     */
    public void put(int key,MessagePackMap value) {
        map.put(Integer.valueOf(key),value);
    }
    
    /**
     * Puts a value in the map.
     *
     * @param key The key.
     * @param value The value.
     */
    void put(Object key,Object value) {
        map.put(key,value);
    }
    
    /**
     * Gets the value of a key.
     *
     * @param key The key.
     * @return The value.
     */
    public Object get(Object key) {
        return map.get(key);
    }
    
    /**
     * Checks if a map containts a value under given key.
     *
     * @param key The key.
     * @return <code>true</code> if contains, <code>false</code> otherwise.
     */
    public boolean contains(Object key) {
        return get(key) != null;
    }
    
    /**
     * Checks if a map containts a value under given key.
     *
     * @param key The key.
     * @return <code>true</code> if contains, <code>false</code> otherwise.
     */
    public boolean contains(int key) {
        return contains(Integer.valueOf(key));
    }
    
    /**
     * Gets an integer value.
     * 
     * @param key The key.
     * @return The integer value.
     */
    public Integer getInt(Object key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }        
        if (value instanceof Integer) {
            return (Integer)value;
        }
        
        throw new IllegalStateException("Attempt to get a non-integer value");
    }
    
    /**
     * Gets an integer value.
     * 
     * @param key The key.
     * @param defaultValue The default value.
     * @return The map value under the key or default value if there is no such
     *   map value.
     */
    public int getInt(Object key,int defaultValue) {
        Integer value = getInt(key);
        if (value == null) {
            return defaultValue;            
        }
        return value.intValue();
    }
    
    /**
     * Gets an integer value.
     * 
     * @param key The key.
     * @return The integer value.
     */
    public Integer getInt(int key) {
        return getInt(Integer.valueOf(key));
    }
    
    /**
     * Gets a string value.
     * 
     * @param key The key.
     * @return The string value.
     */
    public String getStr(Object key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }        
        if (value instanceof String) {
            return (String)value;
        }
        
        throw new IllegalStateException("Attempt to get a non-string value");        
    }
    
    /**
     * Gets a string value.
     * 
     * @param key The key.
     * @return The string value.
     */
    public String getStr(int key) {
        return getStr(Integer.valueOf(key));
    }
    
    /**
     * Gets a child map.
     *
     * @param key The key.
     * @return The child map.     
     */
    public MessagePackMap getMap(Object key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }        
        if (value instanceof MessagePackMap) {
            return (MessagePackMap)value;
        }
        
        throw new IllegalStateException("Attempt to get a non-map value");        
    }
    
        /**
     * Gets a child map.
     *
     * @param key The key.
     * @return The child map.     
     */
    public MessagePackMap getMap(int key) {
        return getMap(Integer.valueOf(key));
    }
    
    /**
     * Gets the map size.
     *
     * @return The map size.
     */
    public int getSize() {
        return map.size();
    }
    
    /**
     * Gets the map keys.
     *
     * @return The keys.
     */
    public Set<Object> getKeys() {
        return map.keySet();
    }
    
    /** */
    private String getIndent(int indent) {
        return StringUtils.repeat("    ",indent);
    }
    
    /** */
    private String pretty(Object value,int indent) {
        if (value instanceof MessagePackMap) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("{\n");
            
            MessagePackMap map = (MessagePackMap)value;            
            for (Object key:map.getKeys()) {
                buffer.append(getIndent(indent) + key + " = ");
                buffer.append(pretty(map.get(key),indent + 1));
            }
            
            buffer.append(getIndent(indent - 1) + "}\n");
            return buffer.toString();
        }
        
        if (value instanceof String) {
            return "'" + value + "'\n";
        }
        
        return value.toString() + "\n";
    }
    
    /** */
    @Override
    public String toString() {
        return pretty(this,1);
    }
}