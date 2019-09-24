#ifndef AE_GLES2_LUA_VERTEX_BUFFER_OBJECT_H
#define AE_GLES2_LUA_VERTEX_BUFFER_OBJECT_H

#include "lua.hpp"

namespace ae {
    
namespace gles2 {
    
namespace lua {
    
/**
 * \brief Loads the vertex buffer object library.
 * \param L The Lua state.
 */
void loadVertexBufferObjectLib(lua_State *L);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_GLES2_LUA_VERTEX_BUFFER_OBJECT_H