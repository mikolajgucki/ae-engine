#ifndef AE_GPU_GLES_LUA_ENGINE_PLUGIN_H
#define AE_GPU_GLES_LUA_ENGINE_PLUGIN_H

#include "GLGPUMiscFuncs.h"
#include "GLTextureFactory.h"
#include "GLLuaEngineDisplayListener.h"
#include "LuaEnginePlugin.h"
#include "LuaEngine.h"

namespace ae {
    
namespace gles {
 
/**
 * \brief The OpenGL ES Lua plugin implementation.
 */
class GPUGLESLuaEnginePlugin:public ::ae::engine::LuaEnginePlugin {
    /// The miscellaneous functions.
    GLGPUMiscFuncs *miscFuncs;
    
    /// The texture factory.
    GLTextureFactory *textureFactory;
    
    /// The display listner.
    GLLuaEngineDisplayListener *displayListener;
    
    /** */
    void deleteObjects();
    
public:
    /** */
    GPUGLESLuaEnginePlugin():LuaEnginePlugin(name()),
        miscFuncs((GLGPUMiscFuncs *)0),
        textureFactory((GLTextureFactory *)0),
        displayListener((GLLuaEngineDisplayListener *)0) {
    }
    
    /** */
    virtual ~GPUGLESLuaEnginePlugin() {
        deleteObjects();
    }
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool configureLuaEngine(::ae::engine::LuaEngineCfg *cfg);
    
    /** */
    virtual bool start();
    
    /** */
    virtual bool stop();
    
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

#endif // AE_GPU_GLES_LUA_ENGINE_PLUGIN_H