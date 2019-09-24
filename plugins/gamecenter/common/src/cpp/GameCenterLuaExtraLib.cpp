#include "lua_gamecenter.h"
#include "LuaEngine.h"
#include "GameCenterLuaExtraLib.h"

using namespace ae;
using namespace ae::engine;
using namespace ae::gamecenter::lua;

namespace ae {
    
namespace gamecenter {
    
/** */
const char *GameCenterLuaExtraLib::getName() {
    return "gamecenter";
}

/** */
bool GameCenterLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadGameCenterLib(getLog(),luaEngine,luaLibGameCenter,this);
    if (chkError()) {
        return false;
    }    
    return true;
} 

/** */
bool GameCenterLuaExtraLib::unloadExtraLib() {
    unloadGameCenterLib(getLog(),luaLibGameCenter);
    return true;
}

} // namespace
    
} // namespace