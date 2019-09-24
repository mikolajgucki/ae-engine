#ifndef AE_DESKTOP_PLUGIN_FLURRY_H
#define AE_DESKTOP_PLUGIN_FLURRY_H

#include "DesktopLuaLibFlurry.h"
#include "DesktopPlugin.h"

namespace ae {
    
namespace flurry {

/**
 * \brief The desktop Flurry plugin.
 */
class DesktopPluginFlurry:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibFlurry *luaLibFlurry;
    
public:
    /** */
    DesktopPluginFlurry():DesktopPlugin(name()),
        luaLibFlurry((DesktopLuaLibFlurry *)0) {
    }
    
    /** */
    virtual ~DesktopPluginFlurry();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "flurry";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_FLURRY_H