/*
-- @module Vec3
-- @group Math
-- @brief Provides functions for 3D vector manipulation.
-- @full Provides functions for 3D vector manipulation. This module is
--   C library loaded by default.
*/
#include <sstream>
#include <memory>

#include "lua_common.h"
#include "Vec3.h"
#include "luaVec3.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace math {
    
namespace lua {
    
/// The name of the Lua user type.
static const char *vec3Name = "Vec3";

/// The name of the Lua metatable.
static const char *vec3MetatableName = "Vec3.metatable";

/** */
Vec3Type *checkVec3Type(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,vec3MetatableName);
    luaL_argcheck(L,data != (void *)0,index,"Vec3 expected");
    return (Vec3Type *)data;
}    
    
/*
-- @name .new
-- @func
-- @brief Creates a vector.
-- @return The new vector object.
-- @func
-- @brief Creates a vector.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @return The new vector object.
-- @func
-- @brief Creates a vector.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param z The Z coordinate.
-- @return The new vector object.
*/
static int vec3New(lua_State *L) {
// x,y,z
    float x = 0;
    float y = 0;
    float z = 0;
    if (lua_isnoneornil(L,1) == 0) {
        x = (float)luaL_checknumber(L,1);
        if (lua_isnoneornil(L,2)) {
            luaPushError(L,"X and Y coordinates required at least");
            return 0;
        }
        
        y = (float)luaL_checknumber(L,2);
        if (lua_isnoneornil(L,3) == 0) {
            z = (float)luaL_checknumber(L,3);
        }
    }        

// vector
    Vec3 *vec = new (nothrow) Vec3();
    if (vec == (Vec3 *)0) {
        luaPushNoMemoryError(L);
        return 0;        
    }
    
// coordinates
    vec->x = x;
    vec->y = y;
    vec->z = z;
    
// user type
    Vec3Type *data = (Vec3Type *)lua_newuserdata(L,sizeof(Vec3Type));
    data->vec = vec;
    
// metatable
    luaL_getmetatable(L,vec3MetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name .inside2DTriangle
-- @func
-- @brief Tests if a 2D point is inside a 2D triagle.
-- @param p The point.
-- @param pa A triangle vertex.
-- @param pb A triangle vertex.
-- @param pc A triangle vertex.
-- @return `true` if inside, `false` otherwise.
*/
static int vec3Inside2DTriangle(lua_State *L) {
    Vec3Type *p = checkVec3Type(L,1);
    Vec3Type *pa = checkVec3Type(L,2);
    Vec3Type *pb = checkVec3Type(L,3);
    Vec3Type *pc = checkVec3Type(L,4);
    
    const float EPSILON = 0.001;
    if (Vec3::inside2DTriangle(*p->vec,*pa->vec,*pb->vec,*pc->vec,EPSILON)) {
        lua_pushboolean(L,AE_LUA_TRUE);
    }
    else {
        lua_pushboolean(L,AE_LUA_FALSE);
    }        
    
    return 1;    
}

/*
-- @name :x
-- @func
-- @brief Gets the X coordinate.
-- @return The X coordinate.
*/
static int vec3X(lua_State *L) {
    Vec3Type *data = checkVec3Type(L);
    lua_pushnumber(L,data->vec->x);
    
    return 1;    
}

/*
-- @name :y
-- @func
-- @brief Gets the Y coordinate.
-- @return The Y coordinate.
*/
static int vec3Y(lua_State *L) {
    Vec3Type *data = checkVec3Type(L);
    lua_pushnumber(L,data->vec->y);
    
    return 1;    
}

/*
-- @name :z
-- @func
-- @brief Gets the Z coordinate.
-- @return The Z coordinate.
*/
static int vec3Z(lua_State *L) {
    Vec3Type *data = checkVec3Type(L);
    lua_pushnumber(L,data->vec->z);
    
    return 1;    
}

/*
-- @name :__tostring
-- @func
-- @brief Gets the string representation of the vector.
-- @return The string representation of the vector.
*/
static int vec3__tostring(lua_State *L) {
    Vec3Type *data = checkVec3Type(L);
    
    ostringstream str;
    str << "["
        << data->vec->x << ","
        << data->vec->y << ","
        << data->vec->z << "]";
    lua_pushstring(L,str.str().c_str());
    
    return 1;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the vector.
-- @full Destroys the vector. Never call this function directly.
*/
static int vec3__gc(lua_State *L) {
    Vec3Type *data = checkVec3Type(L);
    delete data->vec;
    
    return 0;
}

/** The type functions. */
static const struct luaL_Reg vec3Funcs[] = {    
    {"new",vec3New},
    {"inside2DTriangle",vec3Inside2DTriangle},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg vec3Methods[] = {
    {"x",vec3X},
    {"y",vec3Y},
    {"z",vec3Z},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg vec3Metamethods[] = {
    {"__gc",vec3__gc},
    {"__tostring",vec3__tostring},
    {0,0}
};

/** */
static int vec3RequireFunc(lua_State *L) {
    luaLoadUserType(L,vec3MetatableName,vec3Funcs,vec3Methods,vec3Metamethods);
    return 1;
}

/** */
void loadVec3Lib(lua_State *L) {
    luaL_requiref(L,vec3Name,vec3RequireFunc,1);
    lua_pop(L,1);    
}
    
} // namespace
    
} // namespace
    
} // namespace