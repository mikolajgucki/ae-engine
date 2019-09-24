#ifndef AE_LUA_MAT4_H
#define AE_LUA_MAT4_H

#include "lua.hpp"
#include "Mat4.h"

namespace ae {
    
namespace math {
    
namespace lua {
    
/**
 * \brief Wraps the 4x4 matrix so that it can be used as a Lua user type.
 */
struct Mat4Type {
    /** */
    Mat4 *matrix;
    
    /** A temporary matrix. */
    Mat4 *ma;
    
    /** A temporary matrix. */
    Mat4 *mb;
};
typedef struct Mat4Type Mat4Type;
    
/**
 * \brief Checks wheter the object at given stack index is a user data of
 *   the 4x4 matrix type. Raises error if the object is not of the type.
 * \param index The Lua stack index.
 * \return The user data of the 4x4 matrix type. 
 */
Mat4Type *checkMat4Type(lua_State *L,int index = 1);

/**
 * \brief Loads the 4x4 matrix library.
 * \param L The Lua state.
 */
void loadMat4Lib(lua_State *L);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_MAT4_H