#ifndef AE_FLURRY_LUA_EXTRA_LIB_H
#define AE_FLURRY_LUA_EXTRA_LIB_H

#include "LuaExtraLib.h"
#include "LuaLibFlurry.h"

namespace ae {

namespace flurry {
    
/**
 * \brief The Flurry Lua extra library.
 */
class FlurryLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The platform-specific implementation of the Lua library.
    LuaLibFlurry *luaLibFlurry;
    
public:
    /** */
    FlurryLuaExtraLib(LuaLibFlurry *luaLibFlurry_):LuaExtraLib(),
        luaLibFlurry(luaLibFlurry_) {
    }
    
    /** */
    virtual ~FlurryLuaExtraLib() {
        if (luaLibFlurry != (LuaLibFlurry *)0) {
            delete luaLibFlurry;
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

#endif // AE_FLURRY_LUA_EXTRA_LIB_H