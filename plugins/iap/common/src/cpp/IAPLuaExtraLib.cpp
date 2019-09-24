#include "lua_iap.h"
#include "LuaEngine.h"
#include "IAPLuaExtraLib.h"

using namespace ae;
using namespace ae::engine;
using namespace ae::iap::lua;

namespace ae {
    
namespace iap {
    
/** */
const char *IAPLuaExtraLib::getName() {
    return "iap";
}

/** */
bool IAPLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadIAPLib(getLog(),luaEngine,luaLibIap,this);
    
    if (chkError()) {
        return false;
    }    
    return true;
} 

/** */
bool IAPLuaExtraLib::unloadExtraLib() {
    return true;
}
    
} // namespace
    
} // namespace