#include "Log.h"
#include "lua_flurry.h"
#include "LuaEngine.h"
#include "FlurryLuaExtraLib.h"

using namespace ae;
using namespace ae::engine;
using namespace ae::flurry::lua;

namespace ae {
    
namespace flurry {

/** */
const char *FlurryLuaExtraLib::getName() {
    return "flurry";
}    
    
/** */
bool FlurryLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadFlurryLib(luaEngine,luaLibFlurry,this);
    if (chkError()) {
        return false;
    }    
    
    return true;
} 

/** */
bool FlurryLuaExtraLib::unloadExtraLib() {
    return true;
}
    
} // namespace
    
} // namespace