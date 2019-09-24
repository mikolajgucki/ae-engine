#include <memory>
#include "StandardOutputStream.h"

using namespace std;

namespace ae {
    
namespace io {
    
/** */
StandardOutputStream *StandardOutputStream::instance =
    (StandardOutputStream *)0;
    
/** */
bool StandardOutputStream::write(int value) {
    unsigned char ch = (unsigned char)(value & 0xff);
    size_t size = fwrite(&ch,1,1,stdout);
    if (size == 0) {
        setError("Error writing to the standard output");
        return false;
    }    
    
    return true;
}

/** */
bool StandardOutputStream::flush() {
    if (fflush(stdout) != 0) {
        if (ferror(stdout) != 0) {
            setError("Error flushing standard output stream");
            return false;
        }
    }
    
    return true;
}

/** */
StandardOutputStream *StandardOutputStream::getInstance() {
    if (instance == (StandardOutputStream *)0) {
        instance = new (nothrow) StandardOutputStream();
    }
    
    return instance;
}
    
} // namespace
    
} // namespace