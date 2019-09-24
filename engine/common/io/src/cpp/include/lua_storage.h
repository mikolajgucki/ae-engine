#ifndef AE_IO_LUA_STORAGE_H
#define AE_IO_LUA_STORAGE_H

#include "lua.hpp"
#include "Storage.h"

namespace ae {
    
namespace io {

namespace lua {
    
/**
 * \brief Loads the read-write storage library.
 * \param L The Lua state.
 * \param storage The storage.
 */
void loadStorageLib(lua_State *L,Storage *storage);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_IO_LUA_STORAGE_H