#ifndef AE_LUA_VEC3_H
#define AE_LUA_VEC3_H

#include "lua.hpp"
#include "Vec3.h"

namespace ae {
    
namespace math {
    
namespace lua {
    
/**
 * \brief Wraps the 3D vector so that it can be used a Lua user type.
 */
struct Vec3Type {
    /** */
    Vec3 *vec;
};
typedef struct Vec3Type Vec3Type;
 
/**
 * \brief Checks wheter the object at given stack index is a user data of
 *   the 3D vector type. Raises error if the object is not of the type.
 * \param index The Lua stack index.
 * \return The user data of the 3D vector type. 
 */
Vec3Type *checkVec3Type(lua_State *L,int index = 1);

/**
 * \brief Loads the 3D vector library.
 * \param L The Lua state.
 */
void loadVec3Lib(lua_State *L);

} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_VEC3_H