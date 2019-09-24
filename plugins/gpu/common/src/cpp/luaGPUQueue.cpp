/*
-- @module gpu.GPUQueue
-- @group GPU
-- @brief The default drawer.
*/
#include <sstream>

#include "lua_common.h"
#include "ae_gpu.h"
#include "luaGPUQueue.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {
    
namespace gpu {
    
namespace lua {
    
/// The library name.
static const char *gpuQueueName = "GPUQueue";    
    
/// The name of the Lua metatable.
static const char *gpuQueueMetatableName = "GPUQueue.metatable";

/// The name of the Lua global holding the default drawer factory.
static const char *gpuQueueGlobalFactory = "gpu_GPUQueueFactory";

/**
 * \brief Gets the GPU queue factory from the Lua state.
 * \param L The Lua state.
 * \return The GPU queue factory.
 */
static GPUQueueFactory *getGPUQueueFactory(lua_State *L) {
    lua_getglobal(L,gpuQueueGlobalFactory);
    GPUQueueFactory *factory = (GPUQueueFactory *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return factory;
}

/** */
GPUQueueType *checkGPUQueueType(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,gpuQueueMetatableName);
    luaL_argcheck(L,data != (void *)0,index,"GPUQueue expected");
    return (GPUQueueType *)data;
} 

/*
-- @name .new
-- @func
-- @brief Creates a GPU comment queue.
-- @return The created GPU queue.
*/
static int gpuQueueNew(lua_State *L) {
    GPUQueueFactory *factory = getGPUQueueFactory(L);
    
// create
    GPUQueue *queue = factory->createGPUQueue();
    if (queue == (GPUQueue *)0) {
        luaPushError(L,factory->getError().c_str());
        return 0;
    }
    
// user data
    GPUQueueType *data = (GPUQueueType *)lua_newuserdata(
        L,sizeof(GPUQueueType));
    data->queue = queue;
    
// metatable
    luaL_getmetatable(L,gpuQueueMetatableName);
    lua_setmetatable(L,-2);    
    
    return 1;
}

/*
-- @name .flush
-- @func
-- @brief Executes all the commands in the queue.
*/
static int gpuQueueFlush(lua_State *L) {
    GPUQueueType *data = checkGPUQueueType(L);
    
    if (data->queue->flush() == false) {
        luaPushError(L,data->queue->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :__tostring
-- @func
-- @brief Gets the string representation of the GPU queue.
-- @return The string representation of the GPU queue.
*/
static int gpuQueue__tostring(lua_State *L) {
    ostringstream str; 
    str << getGPUName() << ".GPUQueue []";
    
    lua_pushstring(L,str.str().c_str());
    return 1;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the GPU queue.
-- @full Destroys the GPU queue. Never call this function directly.
*/
static int gpuQueue__gc(lua_State *L) {
    GPUQueueType *data = checkGPUQueueType(L);
    
// check if already deleted
    if (data->queue == (GPUQueue *)0) {
        return 0;
    }
    
// delete
    delete data->queue;
    data->queue = (GPUQueue *)0;
    
    return 0;
}

/** The type functions. */
static const struct luaL_Reg gpuQueueFuncs[] = {
    {"new",gpuQueueNew},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg gpuQueueMethods[] = {
    {"flush",gpuQueueFlush},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg gpuQueueMetamethods[] = {
    {"__tostring",gpuQueue__tostring},
    {"__gc",gpuQueue__gc},
    {0,0}
};

/** */
static int gpuQueueRequireFunc(lua_State *L) {
    luaLoadUserType(L,gpuQueueMetatableName,
        gpuQueueFuncs,gpuQueueMethods,gpuQueueMetamethods);
    return 1;     
}

/** */
void loadGPUQueueLib(LuaEngine *luaEngine,GPUQueueFactory *gpuQueueFactory,
    Error *error) {
// Lua state
    lua_State *L = luaEngine->getLuaState();
    
// global with the GPU queue factory
    lua_pushlightuserdata(L,gpuQueueFactory);
    lua_setglobal(L,gpuQueueGlobalFactory);    
    
// get the GPU global table
    lua_getglobal(L,getGPUName());
    
// push the table key
    lua_pushstring(L,gpuQueueName);
    
// load the library (leaves it on the stack)
    luaL_requiref(L,gpuQueueName,gpuQueueRequireFunc,0);
    
// set the library in the GPU global table
    lua_settable(L,-3);
    
// remove the library from the stack
    lua_pop(L,1);     
}
    
} // namespace
    
} // namespace
    
} // namespace