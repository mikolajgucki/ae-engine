#ifndef AE_DESKTOP_PLUGIN_APP_LOVIN_H
#define AE_DESKTOP_PLUGIN_APP_LOVIN_H

#include "DesktopLuaLibAppLovin.h"
#include "DesktopPlugin.h"

namespace ae {
    
namespace applovin {

/**
 * \brief The desktop AppLovin plugin.
 */
class DesktopPluginAppLovin:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibAppLovin *luaLibAppLovin;
    
public:
    /** */
    DesktopPluginAppLovin():DesktopPlugin(name()),
        luaLibAppLovin((DesktopLuaLibAppLovin *)0) {
    }
    
    /** */
    virtual ~DesktopPluginAppLovin();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "applovin";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_APP_LOVIN_H