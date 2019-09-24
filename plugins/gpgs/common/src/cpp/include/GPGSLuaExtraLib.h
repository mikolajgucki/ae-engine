#ifndef AE_GPGS_LUA_EXTRA_LIB_H
#define AE_GPGS_LUA_EXTRA_LIB_H

#include "LuaExtraLib.h"
#include "LuaLibGPGS.h"

namespace ae {

namespace gpgs {
  
/**
 * \brief The Google Play Game Services Lua extra library.
 */
class GPGSLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The platform-specific implementation of the library.
    LuaLibGPGS *luaLibGpgs;
    
public:
    GPGSLuaExtraLib(LuaLibGPGS *luaLibGpgs_):LuaExtraLib(),
        luaLibGpgs(luaLibGpgs_) {
    }
    
    /** */
    virtual ~GPGSLuaExtraLib() {
        if (luaLibGpgs != (LuaLibGPGS *)0) {
            delete luaLibGpgs;
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

#endif // AE_GPGS_LUA_EXTRA_LIB_H