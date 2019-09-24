#ifndef AE_APPLOVIN_LUA_EXTRA_LIB_H
#define AE_APPLOVIN_LUA_EXTRA_LIB_H

#include "LuaExtraLib.h"
#include "LuaLibAppLovin.h"

namespace ae {
    
namespace applovin {
  
/**
 * \brief AppLovin extra library.
 */
class AppLovinLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The AppLovin platform-specific implementation.
    LuaLibAppLovin *luaLibAppLovin;
    
public:
    /** */
    AppLovinLuaExtraLib(LuaLibAppLovin *luaLibAppLovin_):LuaExtraLib(),
        luaLibAppLovin(luaLibAppLovin_) {
    }
    
    /** */
    virtual ~AppLovinLuaExtraLib() {
        if (luaLibAppLovin != (LuaLibAppLovin *)0) {
            delete luaLibAppLovin;
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

#endif //  AE_APPLOVIN_LUA_EXTRA_LIB_H