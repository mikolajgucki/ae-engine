#ifndef AE_DRAWER_INDEX_LUA_EXTRA_LIB_H
#define AE_DRAWER_INDEX_LUA_EXTRA_LIB_H

#include "DrawerIndexFactory.h"
#include "LuaExtraLib.h"

namespace ae {
    
namespace gpu {
    
/**
 * \brief The Lua extra library wrapping the drawer index factory.
 */
class DrawerIndexLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The GPU queue factory.
    DrawerIndexFactory *drawerIndexFactory;
    
public:
    /** */
    DrawerIndexLuaExtraLib(DrawerIndexFactory *draweIndexFactory_):
        LuaExtraLib(),drawerIndexFactory(draweIndexFactory_) {
    }
    
    /** */
    virtual ~DrawerIndexLuaExtraLib() {
        if (drawerIndexFactory != (DrawerIndexFactory *)0) {
            delete drawerIndexFactory;
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
    
#endif // AE_DRAWER_INDEX_LUA_EXTRA_LIB_H