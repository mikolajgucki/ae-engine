#include "ae_gpu.h"
#include "lua_gpu.h"
#include "GPULuaExtraLib.h"

using namespace ae::engine;
using namespace ae::gpu::lua;

namespace ae {
    
namespace gpu {
    
/** */
const char *GPULuaExtraLib::getName() {
    return getGPUName();
}
    
/** */
bool GPULuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadGPULib(luaEngine,miscFuncs);
    
    if (chkError()) {
        return false;
    }
    return true;
}

/** */
bool GPULuaExtraLib::unloadExtraLib() {
    return true;
}

} // namespace

} // namespace