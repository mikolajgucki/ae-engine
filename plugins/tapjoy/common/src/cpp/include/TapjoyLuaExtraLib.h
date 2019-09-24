#ifndef AE_TAPJOY_LUA_EXTRA_LIB_H
#define AE_TAPJOY_LUA_EXTRA_LIB_H

#include "LuaExtraLib.h"
#include "LuaLibTapjoy.h"

namespace ae {
    
namespace tapjoy {
    
/**
 * \brief Tapjoy Lua extra library.
 */
class TapjoyLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The Tapjoy platform-specific implementation.
    LuaLibTapjoy *luaLibTapjoy;
    
public:
    /** */
    TapjoyLuaExtraLib(LuaLibTapjoy *luaLibTapjoy_):LuaExtraLib(),
        luaLibTapjoy(luaLibTapjoy_) {
    }
    
    /** */
    virtual ~TapjoyLuaExtraLib() {
        if (luaLibTapjoy != (LuaLibTapjoy *)0) {
            delete luaLibTapjoy;
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

#endif // AE_TAPJOY_LUA_EXTRA_LIB_H