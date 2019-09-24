#include "ScopedLock.h"

namespace ae {
    
namespace system {

/** */
ScopedLock::ScopedLock(Mutex *mutex_):mutex(mutex_) {
    mutex->lock();
}

/** */
ScopedLock::~ScopedLock() {
    mutex->unlock();
}

} // namespace

} // namespace