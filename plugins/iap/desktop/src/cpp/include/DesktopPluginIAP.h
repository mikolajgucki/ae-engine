#ifndef AE_DESKTOP_PLUGIN_IAP_H
#define AE_DESKTOP_PLUGIN_IAP_H

#include "DesktopLuaLibIAP.h"
#include "DesktopPlugin.h"

namespace ae {
    
namespace iap {

/**
 * \brief The desktop IAP plugin.
 */
class DesktopPluginIAP:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibIAP *luaLibIAP;
    
public:
    /** */
    DesktopPluginIAP():DesktopPlugin(name()),
        luaLibIAP((DesktopLuaLibIAP *)0) {
    }
    
    /** */
    virtual ~DesktopPluginIAP();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "iap";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_IAP_H