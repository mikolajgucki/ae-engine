#include "Base16OutputStream.h"

namespace ae {
    
namespace io {

/** */
bool Base16OutputStream::open() {
    if (output->open() == false) {
        setError(output->getError());
        return false;
    }
    
    return true;    
}
    
/** */
bool Base16OutputStream::write(int value) {
    unsigned char encoded[] = {0,0};
    base16.encode((unsigned char)value,encoded);
    
    if (output->write(encoded[0]) == false) {
        setError(output->getError());
        return false;
    }
    if (output->write(encoded[1]) == false) {
        setError(output->getError());
        return false;
    }
    
    return true;
}
    
/** */
bool Base16OutputStream::close() {
    if (output->close() == false) {
        setError(output->getError());
        return false;
    }
    
    return true;    
}

} // namespace
    
} // namespace