#ifndef AE_GLES2_LUA_GLSL_UNIFORM_H
#define AE_GLES2_LUA_GLSL_UNIFORM_H

#include "lua.hpp"

namespace ae {
    
namespace gles2 {
    
namespace lua {
    
/**
 * \brief Loads the GLSL uniform library.
 * \param L The Lua state.
 */
void loadGLSLUniformLib(lua_State *L);
    
} // namespace    
    
} // namespace
    
} // namespace

#endif // AE_GLES2_LUA_GLSL_UNIFORM_H