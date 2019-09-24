extern "C" {
#include <windows.h>
}
#include <memory>
#include "win32_error.h"
#include "DynLib.h"

using namespace std;

namespace ae {
    
namespace system {
 
/**
 * \brief The data held in the DynLib class.
 */
struct DynLibData {
    /// The handle to the library.
    HMODULE module;
};
typedef struct DynLibData DynLibData;
    
/** */
bool DynLib::open() {
    if (data != (void *)0) {
        setError("Library already open");
        return false;
    }
    
    DynLibData *dynLibData = new (nothrow) DynLibData();
    if (dynLibData == (DynLibData *)0) {
        setNoMemoryError();
        return false;
    }
    
    dynLibData->module = LoadLibrary(filename.c_str());
    if (dynLibData->module == 0) {
        getLastErrorAsString(this);
        return false;        
    }
    
    data = dynLibData;
    return true;
}

/** */
void *DynLib::getAddress(const std::string& name) {
    if (data == (void *)0) {
        setError("Library not open");
        return (void *)0;
    }

    DynLibData *dynLibData = (DynLibData *)data;
    FARPROC address = GetProcAddress(dynLibData->module,name.c_str());
    if (address == (FARPROC)0) {
        getLastErrorAsString(this);
        return (void *)0;
    }
    
    return (void *)address;
}

/** */
bool DynLib::close() {
    if (data == (void *)0) {
        setError("Library already closed");
        return false;
    }
    
    DynLibData *dynLibData = (DynLibData *)data;
    if (FreeLibrary(dynLibData->module) == 0) {
        getLastErrorAsString(this);
        return false;        
    }
    
    delete dynLibData;
    data = (void *)0;
    return true;
}
    
} // namespace
    
} // namespace