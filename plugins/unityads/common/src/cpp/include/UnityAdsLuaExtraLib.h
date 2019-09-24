#ifndef AE_UNITY_ADS_LUA_EXTRA_LIB_H
#define AE_UNITY_ADS_LUA_EXTRA_LIB_H

#include "LuaExtraLib.h"
#include "LuaLibUnityAds.h"

namespace ae {
    
namespace unityads {
    
/**
 * \brief Unity Ads extra library.
 */
class UnityAdsLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The platform-specific implementation of the library.
    LuaLibUnityAds *luaLibUnityAds;
    
public:
    /** */
    UnityAdsLuaExtraLib(LuaLibUnityAds *luaLibUnityAds_):LuaExtraLib(),
        luaLibUnityAds(luaLibUnityAds_) {
    }
    
    /** */
    virtual ~UnityAdsLuaExtraLib() {
    }
    
    /** */
    virtual const char *getName();
    
    /** */
    virtual bool loadExtraLib(::ae::engine::LuaEngine *luaEngine);   
    
    /** */
    virtual bool unloadExtraLib();    
};
    
} // namespace
    
} // namespace

#endif // AE_UNITY_ADS_LUA_EXTRA_LIB_H