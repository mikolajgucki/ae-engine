#ifndef AE_GPU_LUA_EXTRA_LIB_H
#define AE_GPU_LUA_EXTRA_LIB_H

#include "GPUMiscFuncs.h"
#include "LuaExtraLib.h"

namespace ae {
    
namespace gpu {
  
/**
 * \brief The Lua extra library wrapping the GPU library.
 */
class GPULuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The GPU functions.
    GPUMiscFuncs *miscFuncs;
    
public:
    /** */
    GPULuaExtraLib(GPUMiscFuncs *miscFuncs_):LuaExtraLib(),
        miscFuncs(miscFuncs_) {
    }
    
    /** */
    virtual ~GPULuaExtraLib() {
        if (miscFuncs != (GPUMiscFuncs *)0) {
            delete miscFuncs;
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

#endif // AE_GPU_LUA_EXTRA_LIB_H