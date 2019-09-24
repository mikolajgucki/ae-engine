#include <memory>
#include "BufferedInputStream.h"

using namespace std;

namespace ae {
    
namespace io {
    
/** */   
bool BufferedInputStream::open() {
// create buffer
    if (buffer == (unsigned char *)0) {
        buffer = new (nothrow) unsigned char[BUFFER_SIZE];
        if (buffer == (unsigned char *)0) {
            setNoMemoryError();
            return false;
        }
    }
    
// open
    if (input->open() == false) {
        delete buffer;
        buffer = (unsigned char *)0;
        setError(input->getError());
        return false;
    }
    
    return true;
}    
    
/** */
bool BufferedInputStream::read(int &value) {
    if (eof == true) {
        value = NO_DATA;
        return true;
    }
    
// read buffer if empty
    if (size == NO_DATA) {
        size = input->read(buffer,(size_t)BUFFER_SIZE);
        if (size == ERROR) {
            setError(input->getError());
            return false;
        }
        if (size == 0) {
            eof = true;
            value = NO_DATA;
            return true;
        }
        offset = 0;
    }
    
// next byte from the buffer
    value = buffer[offset];
    offset++;
    if (offset == size) {
        size = NO_DATA;
    }
    
    return true;
}
    
/** */
bool BufferedInputStream::close() {
    if (input->close() == false) {
        setError(input->getError());
        return false;
    }
    return true;
}

} // namespace
    
} // namespace