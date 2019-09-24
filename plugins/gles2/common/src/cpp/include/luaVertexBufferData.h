#ifndef AE_GLES2_LUA_VERTEX_BUFFER_DATA_H
#define AE_GLES2_LUA_VERTEX_BUFFER_DATA_H

#include "lua.hpp"
#include "VertexBufferData.h"

namespace ae {
    
namespace gles2 {
    
namespace lua {
  
/**
 * \brief Wraps the vertex buffer data so that it can be used as Lua user type.
 */
struct VertexBufferDataType {
    /** */
    VertexBufferData *vbd;
};
typedef struct VertexBufferDataType VertexBufferDataType;
    
/**
 * \brief Checks wheter the object at given stack index is a user data of
 *     the vertex buffer data type. Raises error if the object is not of the
 *     type.
 * \param index The Lua stack index.
 * \return The user data of the vertex buffer data type. 
 */
VertexBufferDataType *checkVertexBufferDataType(lua_State *L,int index = 1);

/**
 * \brief Loads the vertex buffer data library.
 * \param L The Lua state.
 */
void loadVertexBufferDataLib(lua_State *L);
    
} // namespace

} // namespace

} // namespace

#endif // AE_GLES2_LUA_VERTEX_BUFFER_DATA_H
