#include "UnhashInputStream.h"
#include "UnhashInputStreamProvider.h"

using namespace std;

namespace ae {
    
namespace io {
    
/** */
InputStream *UnhashInputStreamProvider::getInputStream(const string &id) {
// underlying stream
    InputStream *stream = provider->getInputStream(id);
    if (stream == (InputStream *)0) {
        return (InputStream *)0;
    }
    
// unhash stream
    UnhashInputStream *unhashStream = new (nothrow) UnhashInputStream(stream);
    if (unhashStream == (InputStream *)0) {
        setNoMemoryError();
        return (InputStream *)0;
    }
    
    return unhashStream;
}
    
} // namespace
    
} // namespace