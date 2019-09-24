#include "lua.hpp"
#include "Log.h"
#include "LuaEngine.h"
#include "lua_spine.h"
#include "luaSpineSkeleton.h"
#include "luaSpineSkeletonDrawer.h"
#include "SpineLuaExtraLib.h"

using namespace ae::engine;

namespace ae {
    
namespace spine {

/** */
const char *SpineLuaExtraLib::getName() {
    return "spine";
}

/** */
bool SpineLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    lua_State *L = luaEngine->getLuaState();
    
    ::ae::spine::lua::loadSpineLib(L,this);
    if (chkError() == false) {
        ::ae::spine::lua::loadSpineSkeletonLib(L,getLog());
        ::ae::spine::lua::loadSpineSkeletonDrawerLib(L);
    }
    else {
        return false;
    }
    
    return true;
}
    
/** */
bool SpineLuaExtraLib::unloadExtraLib() {
    return true;
}

} // namespace
    
} // namespace