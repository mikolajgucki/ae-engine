#ifndef AE_UTIL_LUA_MESSAGE_PACK_H
#define AE_UTIL_LUA_MESSAGE_PACK_H

#include "lua.hpp"

namespace ae {
    
namespace util {
    
namespace lua {
    
/**
 * \brief Loads the message pack library.
 * \param L The Lua state.
 */
void loadMessagePackLib(lua_State *L);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_UTIL_LUA_MESSAGE_PACK_H