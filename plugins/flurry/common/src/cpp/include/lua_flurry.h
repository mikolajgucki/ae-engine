#ifndef AE_LUA_FLURRY_H
#define AE_LUA_FLURRY_H

#include "Error.h"
#include "LuaEngine.h"
#include "LuaLibFlurry.h"

namespace ae {
    
namespace flurry {
    
namespace lua {
    
/**
 * \brief Loads the Flurry library.
 * \param luaEngine The Lua engine.
 * \param luaLibFlurry The Flurry Lua library implementation.
 * \param error The error to set if loading fails.
 */
void loadFlurryLib(::ae::engine::LuaEngine *luaEngine,
    LuaLibFlurry *luaLibFlurry,Error *error);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_FLURRY_H