#include <memory>
#include "StandardInputStream.h"

using namespace std;

namespace ae {
    
namespace io {
    
/** */
StandardInputStream *StandardInputStream::instance =
    (StandardInputStream *)0;
    
/** */
bool StandardInputStream::read(int &value) {
    unsigned char ch;
    size_t size = fread(&ch,1,1,stdin);
    if (size == 0) {
        if (ferror(stdin) != 0) {
            setError("Error reading from the standard input");
            return false;
        }
        
        value = NO_DATA;
        return true;
    }    
    
    value = (int)(ch & 0xff);
    return true;
}

/** */
StandardInputStream *StandardInputStream::getInstance() {
    if (instance == (StandardInputStream *)0) {
        instance = new (nothrow) StandardInputStream();
    }
    
    return instance;
}
    
} // namespace
    
} // namespace