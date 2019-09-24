#include "Log.h"
#include "lua_gameanalytics.h"
#include "GameAnalyticsLuaExtraLib.h"

using namespace ae;
using namespace ae::engine;
using namespace ae::gameanalytics::lua;

namespace ae {
    
namespace gameanalytics {

/** */
const char *GameAnalyticsLuaExtraLib::getName() {
    return "gameanalytics";
}

/** */
bool GameAnalyticsLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadGameAnalyticsLib(luaEngine,luaLibGameAnalytics,this);
    
    if (chkError()) {
        return false;
    }
    return true;
} 

/** */
bool GameAnalyticsLuaExtraLib::unloadExtraLib() {
    return true;
}
    
} // namespace
    
} // namespace