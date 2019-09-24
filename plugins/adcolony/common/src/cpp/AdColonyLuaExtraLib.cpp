#include "Log.h"
#include "lua_adcolony.h"
#include "LuaEngine.h"
#include "AdColonyLuaExtraLib.h"

using namespace ae;
using namespace ae::engine;
using namespace ae::adcolony::lua;

namespace ae {

namespace adcolony {
    
/** */
const char *AdColonyLuaExtraLib::getName() {
    return "adcolony";
}

/** */
bool AdColonyLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadAdColonyLib(getLog(),luaEngine,luaLibAdColony,this);
    if (chkError()) {
        return false;
    }
    
    return true;
}    

/** */
bool AdColonyLuaExtraLib::unloadExtraLib() {
    unloadAdColonyLib(getLog(),luaLibAdColony);
    return true;
}
    
} // namespace
    
} // namespace