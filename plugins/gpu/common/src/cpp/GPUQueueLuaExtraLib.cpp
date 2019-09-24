#include <sstream>

#include "ae_gpu.h"
#include "luaGPUQueue.h"
#include "GPUQueueLuaExtraLib.h"

using namespace std;
using namespace ae::engine;
using namespace ae::gpu::lua;

namespace ae {
    
namespace gpu {
    
/** */
const char *GPUQueueLuaExtraLib::getName() {
    ostringstream name;
    name << getGPUName() << ".GPUQueue";
    
    return name.str().c_str();
}
    
/** */
bool GPUQueueLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadGPUQueueLib(luaEngine,gpuQueueFactory,this);
    
    if (chkError()) {
        return false;
    }
    return true;
}

/** */
bool GPUQueueLuaExtraLib::unloadExtraLib() {
    return true;
}

} // namespace
    
} // namespace