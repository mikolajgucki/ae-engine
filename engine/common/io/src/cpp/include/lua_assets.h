#ifndef AE_IO_LUA_ASSETS_H
#define AE_IO_LUA_ASSETS_H

#include "lua.hpp"
#include "Assets.h"

namespace ae {
    
namespace io {

namespace lua {
    
/**
 * \brief Loads the assets library.
 * \param L The Lua state.
 * \param assets The assets access.
 */ 
void loadAssetsLib(lua_State *L,Assets *assets);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_IO_LUA_ASSETS_H