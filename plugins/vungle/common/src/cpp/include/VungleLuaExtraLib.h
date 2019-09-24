#ifndef AE_VUNGLE_LUA_EXTRA_LIB_H
#define AE_VUNGLE_LUA_EXTRA_LIB_H

#include "lua.hpp"
#include "LuaExtraLib.h"
#include "LuaLibVungle.h"

namespace ae {
    
namespace vungle {
    
/**
 * \brief Vungle extra library.
 */
class VungleLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The Vungle Lua library implementation.
    LuaLibVungle *luaLibVungle;
    
public:
    /** */
    VungleLuaExtraLib(LuaLibVungle *luaLibVungle_):LuaExtraLib(),
        luaLibVungle(luaLibVungle_) {
    }
    
    /** */
    virtual ~VungleLuaExtraLib() {
        if (luaLibVungle != (LuaLibVungle *)0) {
            delete luaLibVungle;
        }
    }
    
    /** */
    virtual const char *getName();
    
    /** */
    virtual bool loadExtraLib(::ae::engine::LuaEngine *luaEngine);
    
    /** */
    virtual bool unloadExtraLib();
};
    
} // namespace

} // namespace

#endif // AE_VUNGLE_LUA_EXTRA_LIB_H