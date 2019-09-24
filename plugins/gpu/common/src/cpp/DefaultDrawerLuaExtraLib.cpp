#include <sstream>

#include "ae_gpu.h"
#include "luaDefaultDrawer.h"
#include "DefaultDrawerLuaExtraLib.h"

using namespace std;
using namespace ae::engine;
using namespace ae::gpu::lua;

namespace ae {
    
namespace gpu {
    
/** */
const char *DefaultDrawerLuaExtraLib::getName() {
    ostringstream name;
    name << getGPUName() << ".DefaultDrawer";
    
    return name.str().c_str();
}
    
/** */
bool DefaultDrawerLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadDefaultDrawerLib(luaEngine,defaultDrawerFactory,this);
    if (chkError()) {
        return false;
    }
    return true;
}

/** */
bool DefaultDrawerLuaExtraLib::unloadExtraLib() {
    return true;
}

} // namespace
    
} // namespace