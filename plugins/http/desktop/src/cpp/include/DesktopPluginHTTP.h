#ifndef AE_DESKTOP_PLUGIN_HTTP_H
#define AE_DESKTOP_PLUGIN_HTTP_H

#include "DesktopLuaLibHTTP.h"
#include "DesktopPlugin.h"

namespace ae {

namespace http {
    
/**
 * \brief The desktop HTTP plugin.
 */
class DesktopPluginHTTP:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua library.
    DesktopLuaLibHTTP *luaLibHttp;
    
public:
    /** */
    DesktopPluginHTTP():DesktopPlugin(name()),
        luaLibHttp((DesktopLuaLibHTTP *)0) {
    }
    
    /** */
    virtual ~DesktopPluginHTTP();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "http";
    }
};
    
} // namespace

} // namespace

#endif // AE_DESKTOP_PLUGIN_HTTP_H