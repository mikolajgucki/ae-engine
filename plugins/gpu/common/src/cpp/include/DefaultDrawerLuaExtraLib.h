#ifndef AE_DEFAULT_DRAWER_LUA_EXTRA_LIB_H
#define AE_DEFAULT_DRAWER_LUA_EXTRA_LIB_H

#include "DefaultDrawerFactory.h"
#include "LuaExtraLib.h"

namespace ae {
    
namespace gpu {
  
/**
 * \brief The Lua extra library wrapping the default drawer.
 */
class DefaultDrawerLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The default drawer factory.
    DefaultDrawerFactory *defaultDrawerFactory;
    
public:
    /** */
    DefaultDrawerLuaExtraLib(DefaultDrawerFactory *defaultDrawerFactory_):
        LuaExtraLib(),defaultDrawerFactory(defaultDrawerFactory_) {
    }
    
    /** */
    virtual ~DefaultDrawerLuaExtraLib() {
        if (defaultDrawerFactory != (DefaultDrawerFactory *)0) {
            delete defaultDrawerFactory;
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

#endif //AE_DEFAULT_DRAWER_LUA_EXTRA_LIB_H