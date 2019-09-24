#ifndef AE_UTIL_LUA_MESSAGE_PACK_PACKER_H
#define AE_UTIL_LUA_MESSAGE_PACK_PACKER_H

#include "lua.hpp"

namespace ae {
    
namespace util {
    
namespace lua {
    
/**
 * \brief Loads the message pack unpacker library.
 * \param L The Lua state.
 */
void loadMessagePackPackerLib(lua_State *L);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_UTIL_LUA_MESSAGE_PACK_PACKER_H