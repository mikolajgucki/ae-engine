#include <sstream>

#include "RWopsInputStream.h"
#include "RWopsInputStreamProvider.h"

using namespace std;

namespace ae {
    
namespace io {
 
/** */
InputStream *RWopsInputStreamProvider::getInputStream(const string &filename) {
// combine directory and filename
    ostringstream fullFilename;
    if (dir.empty() == false) {
        fullFilename << dir << "/";
    }
    fullFilename << filename;
    
// create input stream
    RWopsInputStream *stream = new (nothrow) RWopsInputStream(fullFilename.str());
    if (stream == (InputStream *)0) {
        setNoMemoryError();
        return (InputStream *)0;
    }    
    
    return stream;
}
    
} // namespace
    
} // namespace