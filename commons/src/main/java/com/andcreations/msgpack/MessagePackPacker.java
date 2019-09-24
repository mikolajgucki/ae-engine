package com.andcreations.msgpack;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes message pack values.
 *
 * @author Mikolaj Gucki 
 */
public class MessagePackPacker {
    /**
     * Writes a number of bytes.
     *
     * @param output The stream to which to write.
     * @param values The bytes.
     * @throws IOException on I/O error.
     */
    private void write(OutputStream output,int ... values) throws IOException {
        for (int index = 0; index < values.length; index++) {
            output.write(values[index]);
        }
    }
    
    /**
     * Writes a length.
     *
     * @param output The stream to which to write.
     * @param length The length.
     * @throws IOException on I/O error.     
     */
    private void writeLength(OutputStream output,int length)
        throws IOException {
    //
        int v0 = length & 0xff;
        int v1 = (length >> 8) & 0xff;
        write(output,v1,v0);
    }
    
    /**
     * Writes a boolean value.
     *
     * @param output The stream to which to write.
     * @param value The value to write.
     * @throws IOException on I/O error.
     */
    public void writeBool(OutputStream output,boolean value)
        throws IOException {
    //    
        int v0 = value ? MessagePack.MSG_PACK_TRUE : MessagePack.MSG_PACK_FALSE;
        write(output,v0);
    }
    
    /**
     * Writes nil.
     *
     * @param output The stream to which to write.
     * @throws IOException on I/O error.
     */
    public void writeNil(OutputStream output) throws IOException {
        write(output,MessagePack.MSG_PACK_NIL);
    }
    
    /**
     * Writes a 16-bit integer value.
     *
     * @param output The stream to which to write.
     * @param value The value to write.
     * @throws IOException on I/O error.
     */    
    public void writeInt16(OutputStream output,int value) throws IOException {
        int v0 = value & 0xff;
        int v1 = (value >> 8) & 0xff;
        write(output,MessagePack.MSG_PACK_INT16,v1,v0);
    }
    
    /**
     * Writes a 32-bit integer value.
     *
     * @param output The stream to which to write.
     * @param value The value to write.
     * @throws IOException on I/O error.
     */    
    public void writeInt32(OutputStream output,long value) throws IOException {
        int v0 = (int)(value & 0xff);
        int v1 = (int)((value >> 8) & 0xff);
        int v2 = (int)((value >> 16) & 0xff);
        int v3 = (int)((value >> 24) & 0xff);        
        write(output,MessagePack.MSG_PACK_INT32,v3,v2,v1,v0);
    }
    
    /**
     * Writes a 16-bit string value.
     *
     * @param output The stream to which to write.
     * @param value The value to write.
     * @throws IOException on I/O error.
     */  
    public void writeStr16(OutputStream output,String value)
        throws IOException {
    //
        write(output,MessagePack.MSG_PACK_STR16);
        writeLength(output,value.length());
        
        for (int index = 0; index < value.length(); index++) {
            write(output,(int)value.charAt(index));
        }
    }
    
    /**
     * Writes a 16-bit map.
     *
     * @param output The stream to which to write.
     * @param length The number of entries in the map.
     * @throws IOException on I/O error.
     */    
    public void writeMap16(OutputStream output,int length) throws IOException {
        write(output,MessagePack.MSG_PACK_MAP16);
        writeLength(output,length);        
    }
}