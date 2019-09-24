#ifndef AE_DESKTOP_PLUGIN_GLES2_H
#define AE_DESKTOP_PLUGIN_GLES2_H

#include "GLES2LuaEnginePlugin.h"
#include "DesktopPlugin.h"

namespace ae {

namespace gles2 {
    
/**
 * \brief The desktop OpenGL ES 2.0 plugin.
 */
class DesktopPluginGLES2:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua engine plugin.
    GLES2LuaEnginePlugin *gles2luaEnginePlugin;
    
public:
    /** */
    DesktopPluginGLES2():DesktopPlugin(name()),
        gles2luaEnginePlugin((GLES2LuaEnginePlugin *)0) {
    }
    
    /** */
    virtual ~DesktopPluginGLES2();
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaEnginePlugins();    
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "gles2";
    }
};
    
} // namespace

} // namespace

#endif // AE_DESKTOP_PLUGIN_GLES2_H