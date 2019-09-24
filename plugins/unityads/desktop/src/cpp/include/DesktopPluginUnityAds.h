#ifndef AE_DESKTOP_PLUGIN_UNITY_ADS_H
#define AE_DESKTOP_PLUGIN_UNITY_ADS_H

#include "DesktopLuaLibUnityAds.h"
#include "DesktopPlugin.h"

namespace ae {
    
namespace unityads {

/**
 * \brief The desktop UnityAds plugin.
 */
class DesktopPluginUnityAds:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibUnityAds *luaLibUnityAds;
    
public:
    /** */
    DesktopPluginUnityAds():DesktopPlugin(name()),
        luaLibUnityAds((DesktopLuaLibUnityAds *)0) {
    }
    
    /** */
    virtual ~DesktopPluginUnityAds();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "unityads";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_UNITY_ADS_H