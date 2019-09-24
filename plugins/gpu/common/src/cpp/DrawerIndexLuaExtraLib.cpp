#include <sstream>

#include "ae_gpu.h"
#include "luaDrawerIndex.h"
#include "DrawerIndexLuaExtraLib.h"

using namespace std;
using namespace ae::engine;
using namespace ae::gpu::lua;

namespace ae {
    
namespace gpu {
    
/** */
const char *DrawerIndexLuaExtraLib::getName() {
    ostringstream name;
    name << getGPUName() << ".DrawerIndex";
    
    return name.str().c_str();
}
    
/** */
bool DrawerIndexLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadDrawerIndexLib(luaEngine,drawerIndexFactory,this);
    
    if (chkError()) {
        return false;
    }
    return true;
}

/** */
bool DrawerIndexLuaExtraLib::unloadExtraLib() {
    return true;
}

} // namespace
    
} // namespace