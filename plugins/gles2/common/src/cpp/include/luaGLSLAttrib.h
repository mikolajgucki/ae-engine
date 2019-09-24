#ifndef AE_GLES2_LUA_GLSL_ATTRIB_H
#define AE_GLES2_LUA_GLSL_ATTRIB_H

#include "lua.hpp"

namespace ae {
    
namespace gles2 {
    
namespace lua {
    
/**
 * \brief Loads the GLSL attribute library.
 * \param L The Lua state.
 */
void loadGLSLAttribLib(lua_State *L);
    
} // namespace    
    
} // namespace
    
} // namespace

#endif // AE_GLES2_LUA_GLSL_ATTRIB_H