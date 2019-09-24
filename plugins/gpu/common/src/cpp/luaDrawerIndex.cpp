/*
-- @module gpu.DrawerIndex
-- @group GUP
-- @brief The drawer index.
*/
#include <sstream>

#include "lua_common.h"
#include "ae_gpu.h"
#include "luaDrawerIndex.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {

namespace gpu {

namespace lua {
  
/// The library name.
static const char *drawerIndexName = "DrawerIndex";    
    
/// The name of the Lua metatable.
static const char *drawerIndexMetatableName = "DrawerIndex.metatable";

/// The name of the Lua global holding the drawer index factory.
static const char *drawerIndexGlobalFactory = "gpu_DrawerIndexFactory";

/**
 * \brief Gets the drawer index factory from the Lua state.
 * \param L The Lua state.
 * \return The drawer index factory.
 */
static DrawerIndexFactory *getDrawerIndexFactory(lua_State *L) {
    lua_getglobal(L,drawerIndexGlobalFactory);
    DrawerIndexFactory *factory =
        (DrawerIndexFactory *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return factory;
}

/** */
DrawerIndexType *checkDrawerIndexType(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,drawerIndexMetatableName);
    luaL_argcheck(L,data != (void *)0,index,"DrawerIndex expected");
    return (DrawerIndexType *)data;
}

/*
-- @name .new
-- @func
-- @brief Creates a drawer index.
-- @param capacity The number of vertices supported by the index.
-- @return The created drawer index. 
*/
static int drawerIndexNew(lua_State *L) {
    DrawerIndexFactory *factory = getDrawerIndexFactory(L);
    
// capacity
    int capacity = (int)luaL_checkinteger(L,1);
    
// create
    DrawerIndex *index = factory->createDrawerIndex(capacity);
    if (index == (DrawerIndex *)0) {
        luaPushError(L,factory->getError().c_str());
        return 0;        
    }
    
// user data
    DrawerIndexType *data = (DrawerIndexType *)lua_newuserdata(
        L,sizeof(DrawerIndexType));
    data->index = index;
    
// metatable
    luaL_getmetatable(L,drawerIndexMetatableName);
    lua_setmetatable(L,-2);        
    
    return 1;
}

/*
-- @name .setValue
-- @func
-- @brief Sets a value in the drawer index.
-- @param position The position at which to set.
-- @param value The value to set.
*/
static int drawerIndexSetValue(lua_State *L) {
    DrawerIndexType *data = checkDrawerIndexType(L);
    
// position, value
    int position = luaL_checkinteger(L,2);
    int value = luaL_checkinteger(L,3);
    
// set
    if (data->index->setValue(position,value) == false) {
        luaPushError(L,data->index->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :__tostring
-- @func
-- @brief Gets the string representation of the drawer index.
-- @return The string representation of the drawer index.
*/
static int drawerIndex__tostring(lua_State *L) {
    DrawerIndexType *data = checkDrawerIndexType(L);    
    
    ostringstream str; 
    str << getGPUName() << "." << drawerIndexName <<
        " [capacity=" << data->index->getCapacity() << "]";
    
    lua_pushstring(L,str.str().c_str());
    return 1;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the drawer index.
-- @full Destroys the drawer index. Never call this function directly.
*/
static int drawerIndex__gc(lua_State *L) {
    DrawerIndexType *data = checkDrawerIndexType(L);
    
// check if already deleted
    if (data->index == (DrawerIndex *)0) {
        return 0;
    }
    
// delete
    delete data->index;
    data->index = (DrawerIndex *)0;
    
    return 0;
}

/** The type functions. */
static const struct luaL_Reg drawerIndexFuncs[] = {
    {"new",drawerIndexNew},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg drawerIndexMethods[] = {
    {"setValue",drawerIndexSetValue},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg drawerIndexMetamethods[] = {
    {"__tostring",drawerIndex__tostring},
    {"__gc",drawerIndex__gc},
    {0,0}
};

/** */
static int drawerIndexRequireFunc(lua_State *L) {
    luaLoadUserType(L,drawerIndexMetatableName,
        drawerIndexFuncs,drawerIndexMethods,drawerIndexMetamethods);
    return 1;     
}

/** */
void loadDrawerIndexLib(LuaEngine *luaEngine,
    DrawerIndexFactory *drawerIndexFactory,Error *error) {
// Lua state
    lua_State *L = luaEngine->getLuaState();
    
// global with the drawer index factory
    lua_pushlightuserdata(L,drawerIndexFactory);
    lua_setglobal(L,drawerIndexGlobalFactory);     
    
// get the GPU global table
    lua_getglobal(L,getGPUName());
    
// push the table key
    lua_pushstring(L,drawerIndexName);
    
// load the library (leaves it on the stack)
    luaL_requiref(L,drawerIndexName,drawerIndexRequireFunc,0);
    
// set the library in the GPU global table
    lua_settable(L,-3);
    
// remove the library from the stack
    lua_pop(L,1);     
}
    
} // namespace
    
} // namespace

} // namespace