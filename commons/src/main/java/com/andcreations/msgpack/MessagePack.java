package com.andcreations.msgpack;

/**
 * Contains the message pack common values.
 *
 * @author Mikolaj Gucki 
 */
public class MessagePack {
    /** The nil value. */
    public static final int MSG_PACK_NIL = 0xc0;
    
    /** The false value. */
    public static final int MSG_PACK_FALSE = 0xc2;
    
    /** The true value. */
    public static final int MSG_PACK_TRUE = 0xc3;
    
    /** The identifier of a 16-bit signed integer. */
    public static final int MSG_PACK_INT16 = 0xd1;
    
    /** The identifier of a 32-bit signed integer. */
    public static final int MSG_PACK_INT32 = 0xd2;
    
    /** The identifier of a 16-bit string. */
    public static final int MSG_PACK_STR16 = 0xda;
    
    /** The identifier of a 16-bit map. */
    public static final int MSG_PACK_MAP16 = 0xde;
}