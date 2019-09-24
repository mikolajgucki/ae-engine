#ifndef AE_LUA_GPU_H
#define AE_LUA_GPU_H

#include "LuaEngine.h"
#include "GPUMiscFuncs.h"

namespace ae {
    
namespace gpu {
    
namespace lua {
    
/**
 * \brief Loads the GPU library.
 * \param luaEngine The Lua engine.
 * \param miscFuncs The functions.
 */
void loadGPULib(::ae::engine::LuaEngine *luaEngine,
    GPUMiscFuncs *miscFuncs);

} // namespace

} // namespace
    
} // namespace

#endif // AE_LUA_GPU_H