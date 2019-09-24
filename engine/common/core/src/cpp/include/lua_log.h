#ifndef AE_LUA_LOG_H
#define AE_LUA_LOG_H

#include "lua.hpp"

namespace ae {
    
namespace lua {
    
/**
 * \brief Loads the log C-functions.
 * \param luaState The Lua state.
 */
void loadLogLib(lua_State *luaState);    
    
} // namespace
    
} // namespace

#endif // AE_LUA_LOG_H