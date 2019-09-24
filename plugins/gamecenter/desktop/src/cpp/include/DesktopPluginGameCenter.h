#ifndef AE_DESKTOP_PLUGIN_GAME_CENTER_H
#define AE_DESKTOP_PLUGIN_GAME_CENTER_H

#include "DesktopPlugin.h"
#include "DesktopLuaLibGameCenter.h"

namespace ae {
    
namespace gamecenter {

/**
 * \brief The desktop GameCenter plugin.
 */
class DesktopPluginGameCenter:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibGameCenter *luaLibGameCenter;
    
public:
    /** */
    DesktopPluginGameCenter():DesktopPlugin(name()),
        luaLibGameCenter((DesktopLuaLibGameCenter *)0) {
    }
    
    /** */
    virtual ~DesktopPluginGameCenter();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "gamecenter";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_GAME_CENTER_H