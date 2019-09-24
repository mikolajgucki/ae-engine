#include "LuaEngine.h"
#include "lua_vungle.h"
#include "VungleLuaExtraLib.h"

using namespace ae;
using namespace ae::engine;
using namespace ae::vungle::lua;

namespace ae {

namespace vungle {
    
/** */
const char *VungleLuaExtraLib::getName() {
    return "vungle";
}

/** */
bool VungleLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadVungleLib(getLog(),luaEngine,luaLibVungle,this);
    if (chkError()) {
        return false;
    }     
    return true;
}  

/** */
bool VungleLuaExtraLib::unloadExtraLib() {
    unloadVungleLib(getLog(),luaLibVungle);
    return true;
}
    
} // namespace
    
} // namespace