#include "Log.h"
#include "LuaEngine.h"
#include "lua_applovin.h"
#include "AppLovinLuaExtraLib.h"

using namespace ae;
using namespace ae::engine;
using namespace ae::applovin::lua;

namespace ae {

namespace applovin {
    
/** */
const char *AppLovinLuaExtraLib::getName() {
    return "applovin";
}

/** */
bool AppLovinLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadAppLovinLib(getLog(),luaEngine,luaLibAppLovin,this);
    if (chkError()) {
        return false;
    }     
    return true;
}    

/** */
bool AppLovinLuaExtraLib::unloadExtraLib() {
    unloadAppLovinLib(getLog(),luaLibAppLovin);
    return true;
}
    
} // namespace
    
} // namespace