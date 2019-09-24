#ifndef AE_LUA_SPINE_H
#define AE_LUA_SPINE_H

#include "lua.hpp"
#include "LuaEngine.h"
#include "Error.h"

namespace ae {
    
namespace spine {
    
namespace lua {
    
/**
 * \brief Loads the Spine library.
 * \param luaEngine The Lua engine.
 * \param error The error to set if loading fails. 
 */
void loadSpineLib(::ae::engine::LuaEngine *luaEngine,Error *error);

/**
 * \brief Gets the Spine library name.
 * \return The library name.
 */
const char *getSpineLibName();

} // namespace

} // namespace
    
} // namespace

#endif // AE_LUA_SPINE_H