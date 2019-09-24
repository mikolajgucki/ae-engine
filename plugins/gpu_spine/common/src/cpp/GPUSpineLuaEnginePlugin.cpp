#include <string>

#include "Log.h"
#include "LuaEngine.h"
#include "GPULuaEnginePlugin.h"
#include "SpineLuaExtraLib.h"
#include "GPUSpineLuaEnginePlugin.h"

using namespace std;
using namespace ae::gpu;

namespace ae {

namespace spine {
    
/// The log tag.
static const char *logTag = "gpu_spine";
    
/** */
bool GPUSpineLuaEnginePlugin::init() {
// GPU plugin
    GPULuaEnginePlugin *gpuPlugin =
        (GPULuaEnginePlugin *)(getLuaEngine()->getPlugin(string("gpu")));
    if (gpuPlugin == (GPULuaEnginePlugin *)0) {
        setError("Lua engine plugin 'gpu' not found");
        return false;
    }   
    
// default drawer factory
    DefaultDrawerFactory *defaultDrawerFactory =
        gpuPlugin->getDefaultDrawerFactory();
    if (defaultDrawerFactory == (DefaultDrawerFactory *)0) {
        setError("GPU plugin does not provide fefault drawer factory");
        return false;
    }
    
// Spine Lua library
    SpineLuaExtraLib *spineLuaExtraLib =
        new (nothrow) SpineLuaExtraLib(defaultDrawerFactory);
    if (spineLuaExtraLib == (SpineLuaExtraLib *)0) {
        setNoMemoryError();
        return false;
    }
    getLuaEngine()->addExtraLib(spineLuaExtraLib);
    
    return true;
}
    
} // namespace

} // namespace