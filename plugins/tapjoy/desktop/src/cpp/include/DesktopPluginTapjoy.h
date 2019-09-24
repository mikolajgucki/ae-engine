#ifndef AE_DESKTOP_PLUGIN_TAPJOY_H
#define AE_DESKTOP_PLUGIN_TAPJOY_H

#include "DesktopLuaLibTapjoy.h"
#include "DesktopPlugin.h"

namespace ae {

namespace tapjoy {
    
/**
 * \brief The desktop Tapjoy plugin.
 */
class DesktopPluginTapjoy:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibTapjoy *luaLibTapjoy;
    
public:
    /** */
    DesktopPluginTapjoy():DesktopPlugin(name()),
        luaLibTapjoy((DesktopLuaLibTapjoy *)0) {
    }
    
    /** */
    virtual ~DesktopPluginTapjoy();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "tapjoy";
    }
};
    
} // namespace

} // namespace

#endif // AE_DESKTOP_PLUGIN_TAPJOY_H