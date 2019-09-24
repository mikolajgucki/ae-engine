#ifndef AE_GPU_QUEUE_LUA_EXTRA_LIB_H
#define AE_GPU_QUEUE_LUA_EXTRA_LIB_H

#include "GPUQueueFactory.h"
#include "LuaExtraLib.h"

namespace ae {
    
namespace gpu {
  
/**
 * \brief The Lua extra library wrapping the GPU queue factory.
 */
class GPUQueueLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The GPU queue factory.
    GPUQueueFactory *gpuQueueFactory;
    
public:
    /** */
    GPUQueueLuaExtraLib(GPUQueueFactory *gpuQueueFactory_):
        LuaExtraLib(),gpuQueueFactory(gpuQueueFactory_) {
    }
    
    /** */
    virtual ~GPUQueueLuaExtraLib() {
        if (gpuQueueFactory != (GPUQueueFactory *)0) {
            delete gpuQueueFactory;
        }
    }
    
    /** */
    virtual const char *getName();    
    
    /** */
    virtual bool loadExtraLib(::ae::engine::LuaEngine *luaEngine);    
    
    /** */
    virtual bool unloadExtraLib();
};
    
} // namespace
    
} // namespace

#endif // AE_GPU_QUEUE_LUA_EXTRA_LIB_H