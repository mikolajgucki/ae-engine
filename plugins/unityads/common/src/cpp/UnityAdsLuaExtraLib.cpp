#include "lua_unityads.h"
#include "UnityAdsLuaExtraLib.h"

using namespace ae::engine;
using namespace ae::unityads::lua;

namespace ae {
    
namespace unityads {
    
/** */
const char *UnityAdsLuaExtraLib::getName() {
    return "unityads";
}

/** */
bool UnityAdsLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadUnityAdsLib(getLog(),luaEngine,luaLibUnityAds,this);
    if (chkError()) {
        return false;
    }
    
    return true;
}    

/** */
bool UnityAdsLuaExtraLib::unloadExtraLib() {
    unloadUnityAdsLib(getLog(),luaLibUnityAds);
    return true;
}
    
} // namespace
    
} // namespace