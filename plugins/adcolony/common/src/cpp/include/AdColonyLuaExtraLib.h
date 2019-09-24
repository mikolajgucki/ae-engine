#ifndef AE_AD_COLONY_LUA_EXTRA_LIB_H
#define AE_AD_COLONY_LUA_EXTRA_LIB_H

#include "LuaExtraLib.h"
#include "LuaLibAdColony.h"

namespace ae {

namespace adcolony {
  
/**
 * \brief AdColony extra library.
 */
class AdColonyLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The platform-specific implementation of the library.
    LuaLibAdColony *luaLibAdColony;
    
public:
    /** */
    AdColonyLuaExtraLib(LuaLibAdColony *luaLibAdColony_):LuaExtraLib(),
        luaLibAdColony(luaLibAdColony_) {
    }
    
    /** */
    virtual ~AdColonyLuaExtraLib() {
        if (luaLibAdColony != (LuaLibAdColony *)0) {
            delete luaLibAdColony;
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

#endif // AE_AD_COLONY_LUA_EXTRA_LIB_H