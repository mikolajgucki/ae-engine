#ifndef AE_LUA_UNITY_ADS_H
#define AE_LUA_UNITY_ADS_H

#include "DLog.h"
#include "Error.h"
#include "LuaEngine.h"
#include "LuaLibUnityAds.h"

namespace ae {
    
namespace unityads {
    
namespace lua {
    
/**
 * \brief Loads the AdColony library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param luaLibUnityAds The UnityAds library implementation.
 * \param error The error to set if loading fails. 
 */
void loadUnityAdsLib(DLog *log,::ae::engine::LuaEngine *luaEngine,
    LuaLibUnityAds *luaLibUnityAds,Error *error);
    
/**
 * \brief Unloads the Unity Ads library.
 * \param log The log.
 * \param luaLibAdColony The Unity Ads library implementation.
 */
void unloadUnityAdsLib(DLog *log,LuaLibUnityAds *luaLibUnityAds);

} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_UNITY_ADS_H