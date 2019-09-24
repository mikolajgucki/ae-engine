#include "lua_android.h"
#include "AndroidLuaExtraLib.h"

using namespace ae;
using namespace ae::engine;
using namespace ae::android::lua;

namespace ae {
    
namespace android {
    
/** */
const char *AndroidLuaExtraLib::getName() {
    return "android";
}

/** */
bool AndroidLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadAndroidLib(getLog(),luaEngine,this);
    if (chkError()) {
        return false;
    }
    
    return true;
}    

/** */
bool AndroidLuaExtraLib::unloadExtraLib() {
    unloadAndroidLib(getLog());
    return true;
}    
    
} // namespace
    
} // namespace