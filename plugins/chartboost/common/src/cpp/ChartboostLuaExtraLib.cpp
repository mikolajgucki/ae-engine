#include "Log.h"
#include "LuaEngine.h"
#include "lua_chartboost.h"
#include "ChartboostLuaExtraLib.h"

using namespace ae;
using namespace ae::engine;
using namespace ae::chartboost::lua;

namespace ae {

namespace chartboost {

/** */
const char *ChartboostLuaExtraLib::getName() {
    return "chartboost";
}

/** */
bool ChartboostLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadChartboostLib(getLog(),luaEngine,luaLibChartboost,this);
    if (chkError()) {
        return false;
    }
    
    return true;
}

/** */
bool ChartboostLuaExtraLib::unloadExtraLib() {
    unloadChartboostLib(getLog(),luaLibChartboost);
    return true;
}
    
} // namespace
    
} // namespace