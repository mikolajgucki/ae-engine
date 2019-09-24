/*
-- @module spine.SkeletonDrawer
-- @group Animation
-- @brief Draws a [Spine](http://esotericsoftware.com) skeleton.
*/
#include <memory>
#include <sstream>

#include "Log.h"
#include "lua_common.h"
#include "Mat4.h"
#include "luaMat4.h"
#include "lua_spine.h"
#include "luaSpineSkeleton.h"
#include "SpineSkeletonDrawer.h"
#include "luaSpineSkeletonDrawer.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::math;
using namespace ae::math::lua;

namespace ae {
    
namespace spine {
    
namespace lua {
    
/**
 * \brief Wraps the skeleton drawer so that it can be used as Lua user type.
 */
struct SpineSkeletonDrawerType {
    /** */  
    SpineSkeletonDrawer *drawer;
};
typedef struct SpineSkeletonDrawerType SpineSkeletonDrawerType;
    
/// The library name.
static const char *skeletonDrawerName = "SkeletonDrawer";    
    
/// The name of the Lua metatable.
static const char *skeletonDrawerMetatableName =
    "SpineSkeletonDrawer.metatable";
    
/// The identity matrix.
static const Mat4 MAT4_IDENTITY;
    
/** */
static SpineSkeletonDrawerType *checkSpineSkeletonDrawerType(lua_State *L) {
    void *data = luaL_checkudata(L,1,skeletonDrawerMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"SkeletonDrawer expected");
    return (SpineSkeletonDrawerType *)data;
}    
    
/*
-- @name .new
-- @func
-- @brief Creates a skeleton drawer.
-- @return The skeleton drawer.
*/
static int skeletonDrawerNew(lua_State *L) {
// drawer
    SpineSkeletonDrawer *drawer = new (nothrow) SpineSkeletonDrawer();
    if (drawer == (SpineSkeletonDrawer *)0) {
        luaPushNoMemoryError(L);
        return 0;
    }
    if (drawer->chkError()) {
        luaPushError(L,drawer->getError().c_str());
        delete drawer;
        return 0;
    }
    
// user data
    SpineSkeletonDrawerType *data =(SpineSkeletonDrawerType *)lua_newuserdata(
        L,sizeof(SpineSkeletonDrawerType));
    data->drawer = drawer;
    
// metatable
    luaL_getmetatable(L,skeletonDrawerMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name :draw
-- @func
-- @brief Draws a skeleton.
-- @param skeleton The skeleton to draw.
-- @func
-- @brief Draws a skeleton.
-- @param skeleton The skeleton to draw.
-- @param transformation The transformation matrix.
-- @func
-- @brief Draws a skeleton using a color.
-- @param skeleton The skeleton to draw.
-- @param transformation The transformation matrix.
-- @param r The red component of the color.
-- @param g The green component of the color.
-- @param b The blue component of the color.
-- @param a The alpha component of the color.
*/
static int skeletonDrawerDraw(lua_State *L) {
    SpineSkeletonDrawerType *drawerData = checkSpineSkeletonDrawerType(L);
    SpineSkeletonType *skeletonData = checkSpineSkeletonType(L,2);
    
// transformation
    const Mat4 *transformation;
    if (lua_isnoneornil(L,3) == 0) {
        Mat4Type *matType = checkMat4Type(L,3);
        transformation = matType->matrix;
    }
    else {
        transformation = &MAT4_IDENTITY;
    }
    
// color
    Color color(1,1,1,1);
    Color::fromLua(L,4,5,6,7,color);
    
// draw
    if (drawerData->drawer->drawStart(color) == false) {
        luaPushError(L,drawerData->drawer->getError().c_str());
        return 0;
    }
    if (drawerData->drawer->draw(skeletonData->skeleton,
        *transformation) == false) {
    //
        luaPushError(L,drawerData->drawer->getError().c_str());
        return 0;
    }
    if (drawerData->drawer->drawEnd() == false) {
        luaPushError(L,drawerData->drawer->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the skeleton drawer.
-- @full Destroys the skeleton drawer. Never call this function directly.
*/
static int skeletonDrawer__gc(lua_State *L) {
    SpineSkeletonDrawerType *data = checkSpineSkeletonDrawerType(L);
    delete data->drawer;
    
    return 0;
}

/*
-- @name :__tostring
-- @func
-- @brief Gets the string representation of the skeleton drawer.
-- @return The string representation of the skeleton drawer.
*/
static int skeletonDrawer__tostring(lua_State *L) {
    ostringstream str; 
    str << "ae.spine.SkeletonDrawer []";
    
    lua_pushstring(L,str.str().c_str());
    return 1;
}
    
/** The type functions. */
static const struct luaL_Reg skeletonDrawerFuncs[] = {
    {"new",skeletonDrawerNew},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg skeletonDrawerMethods[] = {
    {"draw",skeletonDrawerDraw},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg skeletonDrawerMetamethods[] = {
    {"__gc",skeletonDrawer__gc},
    {"__tostring",skeletonDrawer__tostring},
    {0,0}
};    
    
/** */
static int skeletonDrawerRequireFunc(lua_State *L) {
    luaLoadUserType(L,skeletonDrawerMetatableName,
        skeletonDrawerFuncs,skeletonDrawerMethods,skeletonDrawerMetamethods);
    return 1;    
}

/** */
void loadSpineSkeletonDrawerLib(lua_State *L) {
// get the Spine global table
    lua_getglobal(L,getSpineLibName());
    
// push the table key
    lua_pushstring(L,skeletonDrawerName);
    
// load the library (leaves it on the stack)
    luaL_requiref(L,skeletonDrawerName,skeletonDrawerRequireFunc,0);
    
// set the library in the Spine global table
    lua_settable(L,-3);
    
// remove the library from the stack
    lua_pop(L,1);    
}    
    
} // namespace

} // namespace
    
} // namespace