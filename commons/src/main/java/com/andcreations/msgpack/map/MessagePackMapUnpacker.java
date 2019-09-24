package com.andcreations.msgpack.map;

import java.io.IOException;
import java.io.InputStream;

import com.andcreations.msgpack.MessagePack;
import com.andcreations.msgpack.MessagePackUnpacker;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Unpacks a map from message pack.
 *
 * @author Mikolaj Gucki 
 */
public class MessagePackMapUnpacker {
    /** The string resources. */
    private static StrResources res = new BundleResources(
        MessagePackMapUnpacker.class);    
    
    /** The unpacker. */
    private static MessagePackUnpacker unpacker = new MessagePackUnpacker();
    
    /**
     * Unpacks a value.
     *
     * @param input The stream from which to read.
     * @return The value.
     * @throws IOException on I/O error.
     */
    public static Object unpackValue(InputStream input) throws IOException {
        int type = unpacker.readType(input);
        
    // boolean
        if (type == MessagePack.MSG_PACK_FALSE) {
            return Boolean.FALSE;
        }
        if (type == MessagePack.MSG_PACK_TRUE) {
            return Boolean.TRUE;
        }
        
    // integer
        if (type == MessagePack.MSG_PACK_INT32) {
            return Integer.valueOf(unpacker.readInt32(input));
        }
        
    // string
        if (type == MessagePack.MSG_PACK_STR16) {
            return unpacker.readStr16(input);
        }
        
    // map
        if (type == MessagePack.MSG_PACK_MAP16) {
            MessagePackMap map = new MessagePackMap();
            
            int length = unpacker.readLength(input);
            for (int index = 0; index < length; index++) {
                Object key = unpackValue(input);
                Object value = unpackValue(input);
                
                map.put(key,value);
            }
            
            return map;
        }
        
        int unsignedType = type & 0xff;
        throw new IOException(res.getStr("unknown.type",
            Integer.toString(unsignedType)));
    }
    
    /**
     * Unpacks a map.
     *
     * @param input The stream from which to read.
     * @return The map.
     * @throws IOException on I/O error.
     */
    public static MessagePackMap unpack(InputStream input) throws IOException {
        Object value = unpackValue(input);
        if (!(value instanceof MessagePackMap)) {
            throw new IOException(res.getStr("not.a.map"));
        }
        
        return (MessagePackMap)value;
    }
}