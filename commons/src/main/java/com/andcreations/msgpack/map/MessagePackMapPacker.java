package com.andcreations.msgpack.map;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import com.andcreations.msgpack.MessagePackPacker;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Packs a mapping into message pack.
 *
 * @author Mikolaj Gucki 
 */
public class MessagePackMapPacker {
    /** The string resources. */
    private static StrResources res = new BundleResources(
        MessagePackMapPacker.class);    
    
    /** The packer. */
    private static MessagePackPacker packer = new MessagePackPacker();
    
    /**
     * Packs a map into message pack.
     *
     * @param output The stream to which to write.
     * @param object The value to pack.
     * @throws IOException on I/O error.
     */
    public static void pack(OutputStream output,Object object)
        throws IOException {
    // boolean
        if (object instanceof Boolean) {
            packer.writeBool(output,((Boolean)object).booleanValue());
            return;
        }
            
    // integer
        if (object instanceof Integer) {
            packer.writeInt32(output,((Integer)object).intValue());
            return;
        }
        
    // string
        if (object instanceof String) {
            packer.writeStr16(output,(String)object);
            return;
        }
        
    // map
        if (object instanceof MessagePackMap) {
            MessagePackMap map = (MessagePackMap)object;            
            Set<Object> keys = map.getKeys();
            
        // map
            packer.writeMap16(output,keys.size());
            
        // keys/values
            for (Object key:keys) {
                Object value = map.get(key);
                pack(output,key);
                pack(output,value);
            }
            
            return;
        }
        
        throw new IOException(res.getStr("unknown.type",
            object.getClass().getName()));
    }
}