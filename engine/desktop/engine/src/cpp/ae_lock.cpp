#include "Mutex.h"
#include "ae_lock.h"

using namespace ae::system;

namespace ae {

/// The global lock.
static Mutex globalLock;
    
/** */
bool aeGlobalLock() {
    if (globalLock.isCreated() == false) {
        if (globalLock.create() == false) {
            return false;
        }
    }
    
    globalLock.lock();
    return true;
}

/** */
bool aeGlobalUnlock() {
    globalLock.unlock();
    return true;
}
    
} // namespace