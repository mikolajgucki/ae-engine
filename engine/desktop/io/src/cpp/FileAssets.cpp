#include <memory>
#include <vector>
#include "FileAssets.h"

using namespace std;

namespace ae {
    
namespace io {
 
/** */
void FileAssets::create(const vector<string> &dirs) {
// input stream provider
    inputStreamProvider = new (nothrow) FileInputStreamProvider(dirs);
    if (inputStreamProvider == (FileInputStreamProvider *)0) {
        setNoMemoryError();
        return;
    }    
}

/** */
FileAssets::~FileAssets() {
    if (inputStreamProvider != (FileInputStreamProvider *)0) {
        delete inputStreamProvider;
    }
}

/** */
InputStream *FileAssets::getInputStream(const string &filename) {
    InputStream *stream = inputStreamProvider->getInputStream(filename);
    if (stream == (InputStream *)0) {
        setError(inputStreamProvider->getError());
        return (InputStream *)0;
    }
    
    return stream;
}
    
} // namespace
    
} // namespace