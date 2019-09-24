#ifndef AE_UTIL_MESSAGE_PACK_H
#define AE_UTIL_MESSAGE_PACK_H

namespace ae {

namespace util {
  
/**
 * \brief Contains the message pack common values. 
 */
class MessagePack {
public:
    enum {
        /** The nil value. */
        MSG_PACK_NIL = 0xc0,
        
        /** The false value. */
        MSG_PACK_FALSE = 0xc2,
        
        /** The true value. */
        MSG_PACK_TRUE = 0xc3,
        
        /** The identifier of a 16-bit signed integer. */
        MSG_PACK_INT16 = 0xd1,
        
        /** The identifier of a 32-bit signed integer. */
        MSG_PACK_INT32 = 0xd2,
        
        /** The identifier of a 16-bit string. */
        MSG_PACK_STR16 = 0xda,
        
        /** The identifier of a 16-bit map. */
        MSG_PACK_MAP16 = 0xde
    };
};
    
} // namespace
    
} // namespace

#endif // AE_UTIL_MESSAGE_PACK_H