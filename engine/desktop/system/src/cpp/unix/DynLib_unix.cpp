extern "C" {
#include <dlfcn.h>
}
#include <memory>
#include "DynLib.h"

using namespace std;

namespace ae {
    
namespace system {
 
/**
 * \brief The data held in the DynLib class.
 */
struct DynLibData {
    /// The handle to the library.
    void *module;
};
typedef struct DynLibData DynLibData;
 
/** */
static void getDLError(Error *error,const string &errorMsg) {
    const char *dlErrorMsg = dlerror();
    if (dlErrorMsg != (const char *)0) {
        error->setError(string(dlErrorMsg));
    }
    else {
        error->setError(errorMsg);
    }
}

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
    
    dynLibData->module = dlopen(filename.c_str(),RTLD_NOW);
    if (dynLibData->module == 0) {
        ostringstream err;
        err << "Failed to open dynamic library " << filename;
        getDLError(this,err.str());
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
    void *address = dlsym(dynLibData->module,name.c_str());
    if (address == (void *)0) {
        ostringstream err;
        err << "Failed to get address for symbol " << name;
        getDLError(this,err.str());
        return (void *)0;
    }
    
    return address;
}

/** */
bool DynLib::close() {
    if (data == (void *)0) {
        setError("Library already closed");
        return false;
    }
    
    DynLibData *dynLibData = (DynLibData *)data;
    if (dlclose(dynLibData->module) != 0) {
        getDLError(this,"Failed to close dynamic library");
        return false;        
    }
    
    delete dynLibData;
    data = (void *)0;
    return true;
}
    
} // namespace
    
} // namespace