/*
-- @module gpu
-- @group GPU
-- @brief The GPU miscellaneous functions.
*/
#include "lua_common.h"
#include "ae_gpu.h"
#include "lua_gpu.h"

using namespace ae::lua;

namespace ae {
    
namespace gpu {
    
namespace lua {
    
/// The name of the Lua global holding the functions.
static const char *gpuGlobalMiscFuncs = "gpu_MiscFuncs";

/**
 * \brief Gets the functions from the Lua state.
 * \param L The Lua state.
 * \return The functions
 */
static GPUMiscFuncs *getGPUMiscFuncs(lua_State *L) {
    lua_getglobal(L,gpuGlobalMiscFuncs);
    GPUMiscFuncs *funcs = (GPUMiscFuncs *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return funcs;
}

/*
-- @name .clear
-- @func
-- @brief Clears the display.
*/
static int gpuClear(lua_State *L) {
    GPUMiscFuncs *funcs = getGPUMiscFuncs(L);
    
// clear
    if (funcs->clear() == false) {
        luaPushError(L,funcs->getError().c_str());
        return 0;
    }
    
    return 0;
}

/** */
static const struct luaL_Reg gpuFuncs[] = {
    {"clear",gpuClear},
    {0,0}
};    

/** */
void loadGPULib(::ae::engine::LuaEngine *luaEngine,
    GPUMiscFuncs *miscFuncs) {
// Lua state
    lua_State *L = luaEngine->getLuaState();
    
// global with the misc functions
    lua_pushlightuserdata(L,miscFuncs);
    lua_setglobal(L,gpuGlobalMiscFuncs);     
    
// get the GPU global table
    lua_getglobal(L,getGPUName());    
    
// set functions
    luaL_setfuncs(L,gpuFuncs,0);
    
// remove the library from the stack    
    lua_pop(L,1);
}
    
} // namespace

} // namespace
    
} // namespace