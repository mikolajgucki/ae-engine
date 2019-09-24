/*
-- @module ae.spine.SpineSkeletonDrawer
-- @group Animation
-- @brief Draws a [Spine](http://esotericsoftware.com) skeleton.
*/
#include <memory>
#include <sstream>

#include "Log.h"
#include "lua_common.h"
#include "Mat4.h"
#include "luaMat4.h"
#include "luaGPUQueue.h"
#include "lua_spine.h"
#include "luaSpineSkeleton.h"
#include "SpineSkeletonDrawer.h"
#include "luaSpineSkeletonDrawer.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::math;
using namespace ae::math::lua;
using namespace ae::engine;
using namespace ae::gpu;
using namespace ae::gpu::lua;

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
static const char *skeletonDrawerName = "SpineSkeletonDrawer";    
    
/// The name of the Lua metatable.
static const char *skeletonDrawerMetatableName =
    "SpineSkeletonDrawer.metatable";
    
/// The name of the Lua global holding the default drawer factory.
static const char *defaultDrawerGlobalFactory =
    "gpu_spine_DefaultDrawerFactory";    
    
/// The identity matrix.
static const Mat4 MAT4_IDENTITY;
    
/**
 * \brief Gets the default drawer factory from the Lua state.
 * \param L The Lua state.
 * \return The default drawer factory.
 */
static DefaultDrawerFactory *getDefaultDrawerFactory(lua_State *L) {
    lua_getglobal(L,defaultDrawerGlobalFactory);
    DefaultDrawerFactory *factory =
        (DefaultDrawerFactory *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return factory;
}

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
-- @param skeleton The skeleton to draw.
-- @return The skeleton drawer.
*/
static int skeletonDrawerNew(lua_State *L) {
    SpineSkeletonType *skeletonData = checkSpineSkeletonType(L,1);
    
// drawer
    SpineSkeletonDrawer *drawer = new (nothrow) SpineSkeletonDrawer(
        getDefaultDrawerFactory(L),skeletonData->skeleton);
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
-- @name :enqueue
-- @func
-- @brief Enqeues a command to draw the skeleton.
-- @param queue The destination GPU queue.
-- @param transformation The transformation matrix.
-- @func
-- @brief Enqeues a command to draw the skeleton.
-- @param queue The destination GPU queue.
-- @param transformation The transformation matrix.
-- @param r The red component (0-1).
-- @param g The green component (0-1).
-- @param b The blue component (0-1)
-- @param a The alpha component (0-1).
*/
static int skeletonDrawerEnqueue(lua_State *L) {
    SpineSkeletonDrawerType *drawerData = checkSpineSkeletonDrawerType(L);
    GPUQueueType *queueData = checkGPUQueueType(L,2);
    Mat4Type *transformationData = checkMat4Type(L,3);
    
// color
    Color color(Color::WHITE);
    Color::fromLua(L,4,5,6,7,color);    
    
    SpineSkeletonDrawer *drawer = drawerData->drawer;
    if (drawer->enqueue(queueData->queue,*(transformationData->matrix),
        color) == false) {
    //
        luaPushError(L,drawer->getError().c_str());
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
    lua_pushstring(L,"ae.spine.SkeletonDrawer []");
    return 1;
}
    
/** The type functions. */
static const struct luaL_Reg skeletonDrawerFuncs[] = {
    {"new",skeletonDrawerNew},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg skeletonDrawerMethods[] = {
    {"enqueue",skeletonDrawerEnqueue},
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
void loadSpineSkeletonDrawerLib(LuaEngine *luaEngine,
    DefaultDrawerFactory *defaultDrawerFactory) {
// Lua state
    lua_State *L = luaEngine->getLuaState();
    
// global with the default drawer factory
    lua_pushlightuserdata(L,defaultDrawerFactory);
    lua_setglobal(L,defaultDrawerGlobalFactory);  

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