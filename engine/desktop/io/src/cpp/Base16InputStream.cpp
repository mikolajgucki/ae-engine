#include "Base16InputStream.h"

namespace ae {
    
namespace io {

/** */
bool Base16InputStream::open() {
    if (input->open() == false) {
        setError(input->getError());
        return false;
    }
    
    return true;    
}
    
/** */
bool Base16InputStream::read(int &value) {
    int ch0;
    if (input->read(ch0) == false) {
        setError(input->getError());
        return false;
    }
    
    int ch1;
    if (input->read(ch1) == false) {
        setError(input->getError());
        return false;
    }
    
    int v0;
    if (base16.decode((unsigned char)ch0,v0) == false) {
        setError("Invalid Base16 character");
        return false;
    }
    
    int v1;
    if (base16.decode((unsigned char)ch1,v1) == false) {
        setError("Invalid Base16 character");
        return false;
    }
    
    value = (v1 << 4) + v0;     
    return true;
}
    
/** */
bool Base16InputStream::close() {
    if (input->close() == false) {
        setError(input->getError());
        return false;
    }
    
    return true;    
}

} // namespace
    
} // namespace