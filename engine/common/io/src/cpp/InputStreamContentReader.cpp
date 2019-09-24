#include "InputStreamContentReader.h"

using namespace std;

namespace ae {

namespace io {
    
/** */
bool InputStreamContentReader::readAll(string &content) {    
    int value;
    char ch;
    
    while (true) {
        if (stream->read(value) == false) {
            setError(stream->getError());
            return false;
        }
        if (value == InputStream::NO_DATA) {
            break;
        }
        
        ch = (char)value;
        content.append(&ch,1);
    }
    
    return true;
}
    
}
    
} // namespace
