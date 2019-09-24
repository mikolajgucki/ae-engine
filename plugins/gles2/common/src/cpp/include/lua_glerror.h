#ifndef AE_GLES2_LUA_GL_ERROR_H
#define AE_GLES2_LUA_GL_ERROR_H

#include "lua.hpp"

namespace ae {
    
namespace gles2 {
    
namespace lua {
    
/**
 * \brief Checks if GL error is set. Push the error onto the Lua state and
 *     reports Lua error.
 * \param L The Lua state.
 * \return <code>true</code> on GL error, <code>false</code> otherwise.
 */
bool luaChkGLError(lua_State *L);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_GLES2_LUA_GL_ERROR_H