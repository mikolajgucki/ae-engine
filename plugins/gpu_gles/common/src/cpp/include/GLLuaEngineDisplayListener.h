#ifndef AE_GL_LUA_ENGINE_DISPLAY_LISTENER_H
#define AE_GL_LUA_ENGINE_DISPLAY_LISTENER_H

#include "LuaEngineDisplayListener.h"

namespace ae {

namespace gles {
    
/**
 * \brief The OpenGL display listener.
 */
class GLLuaEngineDisplayListener:
    public ::ae::engine::LuaEngineDisplayListener {
public:
    /** */
    GLLuaEngineDisplayListener():LuaEngineDisplayListener() {
    }
    
    /** */
    virtual ~GLLuaEngineDisplayListener() {
    }
    
    /** */
    void luaEngineDisplayResized(int width,int height);      
};

} // namespace

} // namespace

#endif // AE_GL_LUA_ENGINE_DISPLAY_LISTENER_H