#include <memory>
#include "Mutex.h"
extern "C" {
#include <windows.h>
}

using namespace std;

namespace ae {

namespace system {

/**
 * \brief The mutex data held in the Mutex class.
 */
struct MutexData {
    /** The nandle to the mutex. */
    HANDLE handle;
};
typedef struct MutexData MutexData;
   
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
    
    mutexData->handle = CreateMutex((LPSECURITY_ATTRIBUTES)0,FALSE,(LPCTSTR)0);
    if (mutexData->handle == (HANDLE)0) {
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
    WaitForSingleObject(mutexData->handle,INFINITE);
}

/** */
void Mutex::unlock() {
    MutexData *mutexData = (MutexData *)data; 
    ReleaseMutex(mutexData->handle);
}
    
} // namespace

} // namespace
