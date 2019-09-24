#ifndef AE_SPINE_LUA_SKELETON_DRAWER_H
#define AE_SPINE_LUA_SKELETON_DRAWER_H

#include "lua.hpp"
#include "LuaEngine.h"
#include "DefaultDrawerFactory.h"

namespace ae {
    
namespace spine {
    
namespace lua {
    
/**
 * \brief Loads the Spine skeleton drawer library.
 * \param luaEngine The Lua engine.
 * \param defaultDrawerFactory The default drawer factory.
 */
void loadSpineSkeletonDrawerLib(::ae::engine::LuaEngine *luaEngine,
    ::ae::gpu::DefaultDrawerFactory *defaultDrawerFactory);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_SPINE_LUA_SKELETON_DRAWER_H