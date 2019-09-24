#include "MessagePackUnpacker.h"

using namespace std;
using namespace ae::io;

namespace ae {

namespace util {
    
/** */
bool MessagePackUnpacker::read(InputStream *input,int &v0) {
    if (input->read(v0) == false) {
        setError(input->getError());
        return false;
    }
    if (v0 == InputStream::NO_DATA) {
        setError("Unexpected end of file");
        return false;
    }
    
    return true;
}

/** */
bool MessagePackUnpacker::readLength(InputStream *input,unsigned int &length) {
    int v0;
    if (read(input,v0) == false) {
        return false;
    }
    
    int v1;
    if (read(input,v1) == false) {
        return false;
    }
    
    length = (((unsigned int)v0) << 8) + (unsigned int)v1;
    return true;
}

/** */
bool MessagePackUnpacker::readType(InputStream *input,int &type) {
    if (input->read(type) == false) {
        setError(input->getError());
        return false;
    }
    if (type == InputStream::NO_DATA) {
        type = END_OF_FILE;
        return true;
    }
    
    return true;
}

/** */
bool MessagePackUnpacker::readInt16(InputStream *input,int &value) {
    int v0;
    if (read(input,v0) == false) {
        return false;
    }
    
    int v1;
    if (read(input,v1) == false) {
        return false;
    }
    
    value = (v0 << 8) + v1;
    return true;
}


/** */
bool MessagePackUnpacker::readInt32(InputStream *input,long &value) {
    int v0;
    if (read(input,v0) == false) {
        return false;
    }
    
    int v1;
    if (read(input,v1) == false) {
        return false;
    }
    
    int v2;
    if (read(input,v2) == false) {
        return false;
    }
    
    int v3;
    if (read(input,v3) == false) {
        return false;
    }
    
    value = (v0 << 24) + (v1 << 16) + (v2 << 8) + v3;
    return true;
}

/** */
bool MessagePackUnpacker::readStr16(InputStream *input,string &value) {
    unsigned int length;
    if (readLength(input,length) == false) {
        return false;
    }
    
    value.reserve(length);
    value.resize(length);
    for (unsigned int index = 0; index < length; index++) {
        int ch;
        if (read(input,ch) == false) {
            return false;
        }
        
        value[index] = (char)ch;
    }
    
    return true;
}

/** */
bool MessagePackUnpacker::readMap16(InputStream *input,unsigned int &length) {
    int v0;
    if (read(input,v0) == false) {
        return false;
    }
    
    int v1;
    if (read(input,v1) == false) {
        return false;
    }
    
    length = (((unsigned int)v0) << 8) + (unsigned int)v1;
    return true;
}
    
} // namespace

} // namespace