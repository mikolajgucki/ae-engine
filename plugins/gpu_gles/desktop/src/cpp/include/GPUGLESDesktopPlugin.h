#ifndef AE_GPU_GL_DESKTOP_PLUGIN_H
#define AE_GPU_GL_DESKTOP_PLUGIN_H

#include "GPUGLESLuaEnginePlugin.h"
#include "DesktopPlugin.h"

namespace ae {

namespace gpu {
    
namespace gles {
    
/**
 * \brief The desktop OpenGL ESplugin.
 */
class GPUGLESDesktopPlugin:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua engine plugin.
    ::ae::gles::GPUGLESLuaEnginePlugin *glesLuaEnginePlugin;
    
public:
    /** */
    GPUGLESDesktopPlugin():DesktopPlugin(name()),
        glesLuaEnginePlugin((::ae::gles::GPUGLESLuaEnginePlugin *)0) {
    }
    
    /** */
    virtual ~GPUGLESDesktopPlugin() {
    }
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaEnginePlugins();    
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "gpu_gles";
    }
};
    
} // namespace

} // namespace

} // namespace

#endif // AE_GPU_GL_DESKTOP_PLUGIN_H