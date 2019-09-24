#include "UnhashInputStream.h"

namespace ae {
    
namespace io {
    
/** */
bool UnhashInputStream::open() {
    if (input->open() == false) {
        setError(input->getError());
        return false;
    }
    
    return true;
}
/** */
bool UnhashInputStream::read(int &value) {
    int readValue = NO_DATA;
    if (input->read(readValue) == false) {
        setError(input->getError());
        return false;
    }
    if (readValue == NO_DATA) {
        value = NO_DATA;
        return true;
    }
    return unhash(readValue);
}

/** */
int UnhashInputStream::read(void *data,size_t size) {
    int readSize = input->read(data,size);
    
// unhash
    unsigned char *charData = reinterpret_cast<unsigned char *>(data);
    unsigned char readValue;
    for (int index = 0; index < readSize; index++) {
        readValue = charData[index];
        charData[index] = unhash(readValue);
    }
    return readSize;
}

/** */
bool UnhashInputStream::close() {
    if (input->close() == false) {
        setError(input->getError());
        return false;
    }
    
    return true;
}
    
} // namespace
    
} // namespace