#include <sstream>

#include "RWopsOutputStream.h"
#include "RWopsOutputStreamProvider.h"

using namespace std;

namespace ae {
    
namespace io {
 
/** */
OutputStream *RWopsOutputStreamProvider::getOutputStream(
    const std::string &filename) {
// combine directory and filename
    ostringstream fullFilename;
    fullFilename << dir << "/" << filename;
    
// create input stream
    RWopsOutputStream *stream = new RWopsOutputStream(fullFilename.str());
    if (stream == (OutputStream *)0) {
        setNoMemoryError();
        return (OutputStream *)0;
    }
    
    return stream;
}    

} // namespace
    
} // namespace