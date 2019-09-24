#ifndef AE_DESKTOP_PLUGIN_AD_COLONY_H
#define AE_DESKTOP_PLUGIN_AD_COLONY_H

#include "DesktopLuaLibAdColony.h"
#include "DesktopPlugin.h"

namespace ae {
    
namespace adcolony {

/**
 * \brief The desktop AdColony plugin.
 */
class DesktopPluginAdColony:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibAdColony *luaLibAdColony;
    
public:
    /** */
    DesktopPluginAdColony():DesktopPlugin(name()),
        luaLibAdColony((DesktopLuaLibAdColony *)0) {
    }
    
    /** */
    virtual ~DesktopPluginAdColony();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "adcolony";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_AD_COLONY_H