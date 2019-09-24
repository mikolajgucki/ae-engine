#ifndef AE_IAP_LUA_EXTRA_LIB_H
#define AE_IAP_LUA_EXTRA_LIB_H

#include "LuaExtraLib.h"
#include "LuaLibIAP.h"

namespace ae {
    
namespace iap {
  
/**
 * \brief The in-app purchases Lua extra library.
 */
class IAPLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The platform-specific implementation of the library.
    LuaLibIAP *luaLibIap;
    
public:
    /** */
    IAPLuaExtraLib(LuaLibIAP *luaLibIap_):LuaExtraLib(),luaLibIap(luaLibIap_) {
    }
    
    /** */
    virtual ~IAPLuaExtraLib() {
        if (luaLibIap != (LuaLibIAP *)0) {
            delete luaLibIap;
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

#endif // AE_IAP_LUA_EXTRA_LIB_H