#ifndef AE_DESKTOP_PLUGIN_CHARTBOOST_H
#define AE_DESKTOP_PLUGIN_CHARTBOOST_H

#include "DesktopLuaLibChartboost.h"
#include "DesktopPlugin.h"

namespace ae {
    
namespace chartboost {

/**
 * \brief The desktop Chartboost plugin.
 */
class DesktopPluginChartboost:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibChartboost *luaLibChartboost;
    
public:
    /** */
    DesktopPluginChartboost():DesktopPlugin(name()),
        luaLibChartboost((DesktopLuaLibChartboost *)0) {
    }
    
    /** */
    virtual ~DesktopPluginChartboost();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "chartboost";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_CHARTBOOST_H