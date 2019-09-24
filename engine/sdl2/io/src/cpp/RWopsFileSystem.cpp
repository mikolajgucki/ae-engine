#include <memory>
#include <string>

#include "RWopsFileSystem.h"

using namespace std;

namespace ae {
    
namespace io {
 
/** */
void RWopsFileSystem::create(const string &dir) {
    storage = new (nothrow) RWopsStorage(dir);
    if (storage == (RWopsStorage *)0) {
        setNoMemoryError();
        return;
    }
}

/** */
void RWopsFileSystem::destroy() {
    if (storage != (RWopsStorage *)0) {
        delete storage;
    }
}
    
} // namespace
    
} // namespace