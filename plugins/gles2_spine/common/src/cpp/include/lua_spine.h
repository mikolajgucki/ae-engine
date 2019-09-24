#ifndef AE_LUA_SPINE_H
#define AE_LUA_SPINE_H

#include "lua.hpp"
#include "Error.h"

namespace ae {
    
namespace spine {
    
namespace lua {
    
/**
 * \brief Loads the Spine library.
 * \param L The Lua state.
 * \param error The error to set if loading fails. 
 */
void loadSpineLib(lua_State *L,Error *error);

/**
 * \brief Gets the Spine library name.
 * \return The library name.
 */
const char *getSpineLibName();

} // namespace

} // namespace
    
} // namespace

#endif // AE_LUA_SPINE_H