#include "Log.h"
#include "LuaEngine.h"
#include "lua_tapjoy.h"
#include "TapjoyLuaExtraLib.h"

using namespace ae::engine;
using namespace ae::tapjoy::lua;

namespace ae {

namespace tapjoy {
        
/** */
const char *TapjoyLuaExtraLib::getName() {
    return "tapjoy";
}

/** */
bool TapjoyLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadTapjoyLib(getLog(),luaEngine,luaLibTapjoy,this);
    
    if (chkError()) {
        return false;
    }    
    return true;
}

/** */
bool TapjoyLuaExtraLib::unloadExtraLib() {
    unloadTapjoyLib(getLog(),luaLibTapjoy);
    return true;
}

} // namespace

} // namespace