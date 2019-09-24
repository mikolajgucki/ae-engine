#include "Log.h"
#include "LuaEngine.h"
#include "lua_gpgs.h"
#include "GPGSLuaExtraLib.h"

using namespace ae::engine;
using namespace ae::gpgs::lua;

namespace ae {

namespace gpgs {

/** */
const char *GPGSLuaExtraLib::getName() {
    return "gpgs";
}

/** */
bool GPGSLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadGPGSLib(getLog(),luaEngine,luaLibGpgs,this);
    
    if (chkError()) {
        return false;
    }    
    return true;
}

/** */
bool GPGSLuaExtraLib::unloadExtraLib() {
    return true;
}

} // namespace
    
} // namespace