#include "ae_lock.h"
#include "ae_lock_sdl2.h"

using namespace ae::engine;

namespace ae {
 
/// The global lock.
static EngineMutex *mutex = (EngineMutex *)0;
    
/** */
void aeSetGlobalLock(EngineMutex *mutex_) {
    mutex = mutex_;
}
    
/** */
bool aeGlobalLock() {
    if (mutex != (EngineMutex *)0) {
        mutex->lock();
    }
    return true;
}

/** */
bool aeGlobalUnlock() {
    if (mutex != (EngineMutex *)0) {
        mutex->unlock();
    }
    return true;
}
    
} // namespace