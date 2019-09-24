#ifndef AE_LUA_DEFAULT_DRAWER_H
#define AE_LUA_DEFAULT_DRAWER_H

#include "Error.h"
#include "LuaEngine.h"
#include "DefaultDrawerFactory.h"

namespace ae {

namespace gpu {

namespace lua {
   
/**
 * \brief Loads the default drawer library.
 * \param luaEngine The Lua engine.
 * \param defaultDrawerFactory The default drawer factory.
 * \param error The error to set if loading fails. 
 */      
void loadDefaultDrawerLib(::ae::engine::LuaEngine *luaEngine,
    DefaultDrawerFactory *defaultDrawerFactory,Error *error);
    
} // namespace
    
} // namespace

} // namespace

#endif // AE_LUA_DEFAULT_DRAWER_H