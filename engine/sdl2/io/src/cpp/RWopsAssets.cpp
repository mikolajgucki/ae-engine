#include <memory>
#include "RWopsAssets.h"

using namespace std;

namespace ae {
    
namespace io {
    
/** */
void RWopsAssets::create() {
    inputStreamProvider = new (nothrow) RWopsInputStreamProvider("");
    if (inputStreamProvider == (RWopsInputStreamProvider *)0) {
        setNoMemoryError();
        return;        
    }
}

/** */
RWopsAssets::~RWopsAssets() {
    if (inputStreamProvider != (RWopsInputStreamProvider *)0) {
        delete inputStreamProvider;
    }
}

/** */
InputStream *RWopsAssets::getInputStream(const string &filename) {
    InputStream *stream = inputStreamProvider->getInputStream(filename);
    if (stream == (InputStream *)0) {
        setError(inputStreamProvider->getError());
        return (InputStream *)0;
    }
    
    return stream;
}  

} // namespace
    
} // namespace