#include "UnityAdsLuaExtraLib.h"
#include "DesktopPluginUnityAds.h"

namespace ae {
    
namespace unityads {
    
/** */
DesktopPluginUnityAds::~DesktopPluginUnityAds() {
    if (luaLibUnityAds != (DesktopLuaLibUnityAds *)0) {
        delete luaLibUnityAds;
    }
}

/** */
bool DesktopPluginUnityAds::init() {
// create library
    luaLibUnityAds = new DesktopLuaLibUnityAds(getCfg(),getLog());
    return true;
}

/** */
bool DesktopPluginUnityAds::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(new UnityAdsLuaExtraLib(luaLibUnityAds));
    return true;
}
    
} // namespace
    
} // namespace