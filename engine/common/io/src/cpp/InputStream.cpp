#include "InputStream.h"

namespace ae {

namespace io {
    
/** */
int InputStream::read(void *data,size_t size) {
    int value;
    unsigned char *chData = (unsigned char *)data;
    
    int readSize = 0;
    for (size_t index = 0; index < size; index++) {
        if (read(value) == false) {
            return ERROR;
        }
        
        if (value == NO_DATA) {
            return readSize;
        }
        
        readSize++;
        chData[index] = (unsigned char)value;
    }
    
    return readSize;
}
    
} // namespace
    
} // namespace
