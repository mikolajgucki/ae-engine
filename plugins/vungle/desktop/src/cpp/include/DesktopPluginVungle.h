#ifndef AE_DESKTOP_PLUGIN_VUNGLE_H
#define AE_DESKTOP_PLUGIN_VUNGLE_H

#include "DesktopLuaLibVungle.h"
#include "DesktopPlugin.h"

namespace ae {
    
namespace vungle {

/**
 * \brief The desktop Vungle plugin.
 */
class DesktopPluginVungle:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibVungle *luaLibVungle;
    
public:
    /** */
    DesktopPluginVungle():DesktopPlugin(name()),
        luaLibVungle((DesktopLuaLibVungle *)0) {
    }
    
    /** */
    virtual ~DesktopPluginVungle();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "vungle";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_VUNGLE_H