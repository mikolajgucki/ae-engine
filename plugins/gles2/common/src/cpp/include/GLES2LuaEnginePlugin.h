#ifndef AE_GLES2_LUA_ENGINE_PLUGIN_H
#define AE_GLES2_LUA_ENGINE_PLUGIN_H

#include "GLESTextureFactory.h"
#include "GLES2LuaEngineDisplayListener.h"
#include "LuaEnginePlugin.h"
#include "LuaEngine.h"

namespace ae {
    
namespace gles2 {
 
/**
 * \brief The OpenGL ES 2.0 Lua plugin implementation.
 */
class GLES2LuaEnginePlugin:public ::ae::engine::LuaEnginePlugin {
    /// The texture factory.
    GLESTextureFactory *textureFactory;
    
    /// The display listener.
    GLES2LuaEngineDisplayListener *displayListener;
    
public:
    /** */
    GLES2LuaEnginePlugin():LuaEnginePlugin(name()),
        textureFactory((GLESTextureFactory *)0),
        displayListener((GLES2LuaEngineDisplayListener *)0) {
    }
    
    /** */
    virtual ~GLES2LuaEnginePlugin() {
    }
    
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
        return "gles2";
    } 
}; 

} // namespace
    
} // namespace

#endif // AE_GLES2_LUA_ENGINE_PLUGIN_H