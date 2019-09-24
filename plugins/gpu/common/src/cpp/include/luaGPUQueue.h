#ifndef AE_LUA_GPU_QUEUE_H
#define AE_LUA_GPU_QUEUE_H

#include "Error.h"
#include "LuaEngine.h"
#include "GPUQueueFactory.h"
#include "luaGPUQueue.h"

namespace ae {
    
namespace gpu {
    
namespace lua {
    
/**
 * \brief Wraps the GPU queue so that is can be used as Lua user type.
 */
struct GPUQueueType {
    /** */
    GPUQueue *queue;
};
typedef struct GPUQueueType GPUQueueType;
    
/**
 * \brief Checks wheter the object at given stack index is a user data of
 *   the GPU queue type. Raises error if the object is not of the type.
 * \param index The Lua stack index.
 * \return The user data of the GPU queue type. 
 */
GPUQueueType *checkGPUQueueType(lua_State *L,int index = 1);

/**
 * \brief Loads the default drawer library.
 * \param luaEngine The Lua engine.
 * \param gpuQueueFactory The GPU queue factory.
 * \param error The error to set if loading fails. 
 */      
void loadGPUQueueLib(::ae::engine::LuaEngine *luaEngine,
    GPUQueueFactory *gpuQueueFactory,Error *error);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_GPU_QUEUE_H