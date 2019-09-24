#ifndef AE_GLES2_LUA_ENGINE_DISPLAY_LISTENER_H
#define AE_GLES2_LUA_ENGINE_DISPLAY_LISTENER_H

#include "LuaEngineDisplayListener.h"

namespace ae {

namespace gles2 {
    
/**
 * \brief The OpenGL ES 2.0 display listener.
 */
class GLES2LuaEngineDisplayListener:
    public ::ae::engine::LuaEngineDisplayListener {
public:
    /** */
    GLES2LuaEngineDisplayListener():LuaEngineDisplayListener() {
    }
    
    /** */
    virtual ~GLES2LuaEngineDisplayListener() {
    }
    
    /** */
    void luaEngineDisplayResized(int width,int height);      
};

} // namespace

} // namespace

#endif // AE_GLES2_LUA_ENGINE_DISPLAY_LISTENER_H