#include <memory>
#include "Mutex.h"
extern "C" {
#include <pthread.h>
}

using namespace std;

namespace ae {

namespace system {
    
/**
 * \brief The mutex data held in the Mutex class.
 */
struct MutexData {
    /** The mutex. */
    pthread_mutex_t mutex;
};
typedef MutexData MutexData;
    
/** */
Mutex::~Mutex() {
    if (data != (void *)0) {
        MutexData *mutexData = (MutexData *)data;    
        delete mutexData;
    }
}

/** */
bool Mutex::create() {
    MutexData *mutexData = new (nothrow) MutexData();
    if (mutexData == (MutexData *)0) {
        setError("Could not allocate memory");
        return false;
    }
    
    pthread_mutexattr_t attr;
    pthread_mutexattr_init(&attr);
    pthread_mutexattr_settype(&attr,PTHREAD_MUTEX_RECURSIVE);
    
    if (pthread_mutex_init(&mutexData->mutex,&attr) != 0) { 
        delete mutexData;
        setError("Failed to create mutex");
        return false;
    }
    
    data = mutexData;
    return true;
}

/** */
void Mutex::lock() {
    MutexData *mutexData = (MutexData *)data; 
    pthread_mutex_lock(&mutexData->mutex);
}

/** */
void Mutex::unlock() {
    MutexData *mutexData = (MutexData *)data; 
    pthread_mutex_unlock(&mutexData->mutex);
}

} // namespace

} // namespace
