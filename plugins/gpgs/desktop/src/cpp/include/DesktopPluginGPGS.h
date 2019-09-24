#ifndef AE_DESKTOP_PLUGIN_GPGS_H
#define AE_DESKTOP_PLUGIN_GPGS_H

#include "DesktopLuaLibGPGS.h"
#include "DesktopPlugin.h"

namespace ae {
    
namespace gpgs {

/**
 * \brief The desktop GPGS plugin.
 */
class DesktopPluginGPGS:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibGPGS *luaLibGPGS;
    
public:
    /** */
    DesktopPluginGPGS():DesktopPlugin(name()),
        luaLibGPGS((DesktopLuaLibGPGS *)0) {
    }
    
    /** */
    virtual ~DesktopPluginGPGS();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "gpgs";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_GPGS_H