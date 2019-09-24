#ifndef AE_DESKTOP_PLUGIN_GAME_ANALYTICS_H
#define AE_DESKTOP_PLUGIN_GAME_ANALYTICS_H

#include "DesktopLuaLibGameAnalytics.h"
#include "DesktopPlugin.h"

namespace ae {
    
namespace gameanalytics {

/**
 * \brief The desktop GameAnalytics plugin.
 */
class DesktopPluginGameAnalytics:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibGameAnalytics *luaLibGameAnalytics;
    
public:
    /** */
    DesktopPluginGameAnalytics():DesktopPlugin(name()),
        luaLibGameAnalytics((DesktopLuaLibGameAnalytics *)0) {
    }
    
    /** */
    virtual ~DesktopPluginGameAnalytics();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "gameanalytics";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_GAME_ANALYTICS_H