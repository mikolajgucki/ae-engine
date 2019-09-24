#include <memory>
#include <string>

#include "FileFileSystem.h"

using namespace std;

namespace ae {
    
namespace io {
 
/** */
void FileFileSystem::create(const string &dir) {
    storage = new (nothrow) FileStorage(dir);
    if (storage == (FileStorage *)0) {
        setNoMemoryError();
        return;
    }
}

/** */
void FileFileSystem::destroy() {
    if (storage != (FileStorage *)0) {
        delete storage;
    }
}
    
} // namespace
    
} // namespace