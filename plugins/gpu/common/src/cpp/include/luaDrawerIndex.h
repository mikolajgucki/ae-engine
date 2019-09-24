#ifndef AE_LUA_DRAWER_INDEX_H
#define AE_LUA_DRAWER_INDEX_H

#include "Error.h"
#include "LuaEngine.h"
#include "DrawerIndex.h"
#include "DrawerIndexFactory.h"

namespace ae {

namespace gpu {

namespace lua {
   
/**
 * \brief Wraps the default drawer so that it can be used as Lua user type.
 */
struct DrawerIndexType {
    /** */
    DrawerIndex *index;
};
typedef struct DrawerIndexType DrawerIndexType;     
    
/**
 * \brief Checks wheter the object at given stack index is a user data of
 *   the drawer index type. Raises error if the object is not of the type.
 * \param index The Lua stack index.
 * \return The user data of the drawer index type. 
 */
DrawerIndexType *checkDrawerIndexType(lua_State *L,int index = 1);
    
/**
 * \brief Loads the drawer index library.
 * \param luaEngine The Lua engine.
 * \param drawerIndexFactory The drawer index factory.
 * \param error The error to set if loading fails. 
 */      
void loadDrawerIndexLib(::ae::engine::LuaEngine *luaEngine,
    DrawerIndexFactory *drawerIndexFactory,Error *error);
    
} // namespace
    
} // namespace

} // namespace

#endif // AE_LUA_DRAWER_INDEX_H