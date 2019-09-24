#include "MessagePackPacker.h"

using namespace std;
using namespace ae::io;

namespace ae {

namespace util {
    
/** */
bool MessagePackPacker::write(OutputStream *output,int v0) {
    if (output->write(v0) == false) {
        setError(output->getError());
        return false;
    }
    
    return true;
}    

/** */
bool MessagePackPacker::write(OutputStream *output,int v0,int v1) {
    if (output->write(v0) == false) {
        setError(output->getError());
        return false;
    }
    if (output->write(v1) == false) {
        setError(output->getError());
        return false;
    }
    
    return true;    
}    

/** */
bool MessagePackPacker::write(OutputStream *output,int v0,int v1,int v2) {
    if (output->write(v0) == false) {
        setError(output->getError());
        return false;
    }
    if (output->write(v1) == false) {
        setError(output->getError());
        return false;
    }
    if (output->write(v2) == false) {
        setError(output->getError());
        return false;
    }
    
    return true;      
}    
  
/** */
bool MessagePackPacker::write(OutputStream *output,int v0,int v1,int v2,
    int v3) {
//
    if (output->write(v0) == false) {
        setError(output->getError());
        return false;
    }
    if (output->write(v1) == false) {
        setError(output->getError());
        return false;
    }
    if (output->write(v2) == false) {
        setError(output->getError());
        return false;
    }
    if (output->write(v3) == false) {
        setError(output->getError());
        return false;
    }
    
    return true;      
} 

/** */
bool MessagePackPacker::writeLength(OutputStream *output,int length) {
    int v0 = length & 0xff;
    int v1 = (length >> 8) & 0xff;    
    
    return write(output,v1,v0);
}
    
/** */
bool MessagePackPacker::writeBool(OutputStream *output,bool value) {
    int v0 = value ? MessagePack::MSG_PACK_TRUE : MessagePack::MSG_PACK_FALSE;
    return write(output,v0);
}    

/** */
bool MessagePackPacker::writeNil(OutputStream *output) {
    return write(output,MessagePack::MSG_PACK_NIL);
}

/** */
bool MessagePackPacker::writeInt16(OutputStream *output,int value) {
    int v0 = value & 0xff;
    int v1 = (value >> 8) & 0xff;

    return write(output,MessagePack::MSG_PACK_INT16,v1,v0);
}
 
/** */
bool MessagePackPacker::writeInt32(ae::io::OutputStream *output,long value) {
    int v0 = value & 0xff;
    int v1 = (value >> 8) & 0xff;
    int v2 = (value >> 16) & 0xff;
    int v3 = (value >> 24) & 0xff;

    if (write(output,MessagePack::MSG_PACK_INT32) == false) {
        return false;
    }
    return write(output,v3,v2,v1,v0);
}

/** */
bool MessagePackPacker::writeStr16(ae::io::OutputStream *output,
    const string &str) {
// identifier
    if (write(output,MessagePack::MSG_PACK_STR16) == false) {
        return false;        
    }
    
// length
    int length = (int)str.size();
    if (writeLength(output,length) == false) {
        return false;
    }
    
// content
    for (int index = 0; index < length; index++) {
        if (write(output,(int)str[index]) == false) {
            return false;
        }
    }
    
    return true;
}

/** */
bool MessagePackPacker::writeMap16(OutputStream *output,unsigned int length) {
// identifier
    if (write(output,MessagePack::MSG_PACK_MAP16) == false) {
        return false;        
}
    
// length
    if (writeLength(output,length) == false) {
        return false;
    }
    
    return true;
}

} // namespace

} // namespace